package com.pepedevs.dbedwars.messaging;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pepedevs.dbedwars.api.messaging.ChannelMember;
import com.pepedevs.dbedwars.api.messaging.Message;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MessagingChannel implements com.pepedevs.dbedwars.api.messaging.MessagingChannel {

    private final List<ChannelMember> channelMembers;
    private Cache<Message, Long> messageHistory;

    public MessagingChannel() {
        this.channelMembers = new ArrayList<>();
        this.messageHistory =
                CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
    }

    @Override
    public List<ChannelMember> getMembers() {
        return new ArrayList<>(this.channelMembers);
    }

    @Override
    public boolean addChannelMember(ChannelMember member) {
        if (this.channelMembers.contains(member)) return false;
        this.channelMembers.add(member);
        return true;
    }

    @Override
    public boolean removeChannelMember(ChannelMember member) {
        return this.channelMembers.removeIf(this.channelMembers::contains);
    }

    @Override
    public void sendMessage(Message message) {
        for (ChannelMember member : this.channelMembers) {}
    }

    @Override
    public Map<Message, Long> getMessageHistory() {
        Map<Message, Long> history = new HashMap<>(this.messageHistory.asMap());

        List<Map.Entry<Message, Long>> sortingList = new LinkedList<>(history.entrySet());

        sortingList.sort(
                new Comparator<Map.Entry<Message, Long>>() {
                    @Override
                    public int compare(Map.Entry<Message, Long> o1, Map.Entry<Message, Long> o2) {
                        return Float.compare(
                                o1.getValue().floatValue(), o2.getValue().floatValue());
                    }
                });

        Map<Message, Long> returnHistory = new LinkedHashMap<>();

        for (Map.Entry<Message, Long> entry : sortingList) {
            returnHistory.put(entry.getKey(), entry.getValue());
        }

        return returnHistory;
    }
}
