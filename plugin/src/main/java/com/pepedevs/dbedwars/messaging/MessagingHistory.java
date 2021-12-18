package com.pepedevs.dbedwars.messaging;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableMessaging;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class MessagingHistory {

    private final Cache<SentMessage, Long> cache;

    protected MessagingHistory(long cacheTime, int maxMessages) {
        this.cache =
                CacheBuilder.newBuilder()
                        .expireAfterWrite(cacheTime, TimeUnit.SECONDS)
                        .maximumSize(maxMessages)
                        .build();
    }

    protected MessagingHistory(long cacheTime) {
        this.cache =
                CacheBuilder.newBuilder().expireAfterWrite(cacheTime, TimeUnit.SECONDS).build();
    }

    protected static MessagingHistory from(ConfigurableMessaging.ConfigurableHistory configurableHistory) {
        return new MessagingHistory(configurableHistory.getCacheTime(), configurableHistory.getMaxMessages());
    }

    protected void addSentMessage(SentMessage message) {
        cache.put(message, System.currentTimeMillis());
    }

    protected void addSentMessage(SentMessage message, Long timestamp) {
        cache.put(message, timestamp);
    }

    public boolean containsMessage(SentMessage message) {
        return cache.getIfPresent(message) != null;
    }

    public CompletableFuture<Boolean> containsSimilar(Message message) {
        return CompletableFuture.supplyAsync(
                new Supplier<Boolean>() {
                    @Override
                    public Boolean get() {
                        for (Map.Entry<SentMessage, Long> entry : cache.asMap().entrySet()) {
                            if (entry.getKey().getRawMessage().equals(message)) return true;
                        }
                        return false;
                    }
                });
    }

    public CompletableFuture<Set<SentMessage>> getSimilarMessages(SentMessage message) {
        return CompletableFuture.supplyAsync(
                new Supplier<Set<SentMessage>>() {
                    @Override
                    public Set<SentMessage> get() {
                        Set<SentMessage> returnSet = new HashSet<>();
                        for (Map.Entry<SentMessage, Long> entry :
                                MessagingHistory.this.cache.asMap().entrySet()) {
                            if (entry.getKey().getRawMessage().equals(message))
                                returnSet.add(entry.getKey());
                        }
                        return returnSet;
                    }
                });
    }

    public CompletableFuture<Map<SentMessage, Long>> getHistory() {
        return CompletableFuture.supplyAsync(
                new Supplier<Map<SentMessage, Long>>() {
                    @Override
                    public Map<SentMessage, Long> get() {
                        List<Map.Entry<SentMessage, Long>> clone =
                                new ArrayList<>(cache.asMap().entrySet());
                        clone.sort(
                                new Comparator<Map.Entry<SentMessage, Long>>() {
                                    @Override
                                    public int compare(
                                            Map.Entry<SentMessage, Long> o1,
                                            Map.Entry<SentMessage, Long> o2) {
                                        return Long.compare(o1.getValue(), o2.getValue()) * -1;
                                    }
                                });

                        Map<SentMessage, Long> returnMap = new LinkedHashMap<>();

                        for (Map.Entry<SentMessage, Long> entry : clone) {
                            returnMap.put(entry.getKey(), entry.getValue());
                        }

                        return returnMap;
                    }
                });
    }
}
