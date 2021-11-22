package com.pepedevs.dbedwars.messaging;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pepedevs.dbedwars.api.task.CancellableTask;
import me.Abhigya.core.util.Duration;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

public class MessagingChannel {

    private final String key;
    private final EnumChannel channel;
    private final MessagingServer server;
    private final Collection<Audience> members;
    private Audience audience;
    private final Cache<Message, Long> messageHistory;

    protected MessagingChannel(String key, EnumChannel channel, MessagingServer server) {
        this.key = key;
        this.channel = channel;
        this.server = server;
        this.members = new CopyOnWriteArraySet<>();
        this.audience = Audience.audience(this.members);
        this.messageHistory =
                CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
    }

    public String getKey() {
        return this.key;
    }

    public EnumChannel getChannel() {
        return this.channel;
    }

    public Audience getAudiences() {
        return this.audience;
    }

    public Collection<Audience> getMembers() {
        return Collections.unmodifiableCollection(this.members);
    }

    public boolean isRegistered() {
        return this.server.isRegistered(this.getKey());
    }

    public void addMember(Audience member) {
        this.members.add(member);
        this.updateAudience();
    }

    public void addMember(CommandSender sender) {
        this.addMember(this.server.adventure().sender(sender));
    }

    public void addMember(UUID player) {
        this.addMember(this.server.adventure().player(player));
    }

    public void addMembers(Audience... members) {
        this.members.addAll(Arrays.asList(members));
        this.updateAudience();
    }

    public void addMembers(CommandSender... senders) {
        for (CommandSender sender : senders) {
            this.members.add(this.server.adventure().sender(sender));
        }
        this.updateAudience();
    }

    public void addMembers(UUID... players) {
        for (UUID player : players) {
            this.members.add(this.server.adventure().player(player));
        }
        this.updateAudience();
    }

    public void removeMember(Audience audience) {
        this.members.remove(audience);
        this.updateAudience();
    }

    private void updateAudience() {
        this.audience = Audience.audience(this.members);
    }

    public void sendMessage(Message message) {
        if (!this.isRegistered())
            throw new IllegalStateException(
                    "Tried to send message to channel `"
                            + this.getKey()
                            + "` which is not registered!");
        this.getAudiences().sendMessage(message.asComponent());
        this.messageHistory.put(message, System.currentTimeMillis());
    }

    public void sendActionBar(Message message) {
        if (!this.isRegistered())
            throw new IllegalStateException(
                    "Tried to send action bar to channel `"
                            + this.getKey()
                            + "` which is not registered!");
        this.getAudiences().sendActionBar(message.asComponent());
    }

    public void sendActionBar(Message message, Duration duration, long delayMillis) {
        if (!this.isRegistered())
            throw new IllegalStateException(
                    "Tried to send action bar to channel `"
                            + this.getKey()
                            + "` which is not registered!");
        this.server
                .getPlugin()
                .getThreadHandler()
                .submitAsync(
                        new CancellableTask() {
                            final long start = System.currentTimeMillis();
                            long lastSent = 0;

                            @Override
                            public void compute() {
                                lastSent = System.currentTimeMillis();
                                MessagingChannel.this.sendActionBar(message);

                                if (System.currentTimeMillis() - start >= duration.toMillis()) {
                                    this.setCancelled(true);
                                }
                            }

                            @Override
                            public boolean shouldExecute() {
                                return !this.isCancelled()
                                        && System.currentTimeMillis() - lastSent >= delayMillis;
                            }
                        });
    }

    public CancellableTask sendActionBar(Message message, long delayMillis) {
        if (!this.isRegistered())
            throw new IllegalStateException(
                    "Tried to send action bar to channel `"
                            + this.getKey()
                            + "` which is not registered!");
        CancellableTask task =
                new CancellableTask() {
                    long lastSent = 0;

                    @Override
                    public void compute() {
                        lastSent = System.currentTimeMillis();
                        MessagingChannel.this.sendActionBar(message);
                    }

                    @Override
                    public boolean shouldExecute() {
                        return !this.isCancelled()
                                && System.currentTimeMillis() - lastSent >= delayMillis;
                    }
                };
        this.server.getPlugin().getThreadHandler().submitAsync(task);
        return task;
    }

    public void sendBossBar(BossBar bossBar, Duration duration) {
        if (!this.isRegistered())
            throw new IllegalStateException(
                    "Tried to send boss bar change to channel `"
                            + this.getKey()
                            + "` which is not registered!");
        this.showBossBar(bossBar);
        this.server
                .getPlugin()
                .getThreadHandler()
                .submitAsync(
                        new CancellableTask() {
                            final long start = System.currentTimeMillis();

                            @Override
                            public void compute() {
                                this.setCancelled(true);
                                MessagingChannel.this.hideBossBar(bossBar);
                            }

                            @Override
                            public boolean shouldExecute() {
                                return !this.isCancelled()
                                        && System.currentTimeMillis() - this.start
                                                >= duration.toMillis();
                            }
                        });
    }

    public void showBossBar(BossBar bossBar) {
        if (!this.isRegistered())
            throw new IllegalStateException(
                    "Tried to send boss bar change to channel `"
                            + this.getKey()
                            + "` which is not registered!");
        this.getAudiences().showBossBar(bossBar);
    }

    public void hideBossBar(BossBar bossBar) {
        if (!this.isRegistered())
            throw new IllegalStateException(
                    "Tried to send boss bar change to channel `"
                            + this.getKey()
                            + "` which is not registered!");
        this.getAudiences().hideBossBar(bossBar);
    }

    public Map<Message, Long> getMessageHistory() {
        if (!this.isRegistered()) return Collections.EMPTY_MAP;
        Map<Message, Long> history = new HashMap<>(this.messageHistory.asMap());

        List<Map.Entry<Message, Long>> sortingList = new LinkedList<>(history.entrySet());

        sortingList.sort(
                new Comparator<Map.Entry<Message, Long>>() {
                    @Override
                    public int compare(Map.Entry<Message, Long> o1, Map.Entry<Message, Long> o2) {
                        return Long.compare(o1.getValue(), o2.getValue()) * -1;
                    }
                });

        Map<Message, Long> returnHistory = new LinkedHashMap<>();

        for (Map.Entry<Message, Long> entry : sortingList) {
            returnHistory.put(entry.getKey(), entry.getValue());
        }

        return returnHistory;
    }
}
