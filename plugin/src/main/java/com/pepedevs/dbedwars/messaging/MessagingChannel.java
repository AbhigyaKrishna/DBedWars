package com.pepedevs.dbedwars.messaging;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pepedevs.dbedwars.api.task.CancellableTask;
import me.Abhigya.core.util.Duration;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessagingChannel {

    private final EnumChannel channel;
    private final MessagingServer server;
    private final Collection<Audience> members;
    private Audience audience;
    private final Cache<Message, Long> messageHistory;

    public MessagingChannel(EnumChannel channel, Collection<Audience> members) {
        this.channel = channel;
        this.server = MessagingServer.getInstance();
        this.members = members;
        this.audience = Audience.audience(this.members);
        this.messageHistory =
                CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
    }

    public MessagingChannel(EnumChannel channel, Player... players) {
        this(
                channel,
                Arrays.stream(players)
                        .map(
                                new Function<Player, Audience>() {
                                    @Override
                                    public Audience apply(Player player) {
                                        return MessagingServer.getInstance()
                                                .adventure()
                                                .player(player);
                                    }
                                })
                        .collect(Collectors.toSet()));
    }

    public MessagingChannel(EnumChannel channel, UUID... players) {
        this(
                channel,
                Arrays.stream(players)
                        .map(
                                new Function<UUID, Audience>() {
                                    @Override
                                    public Audience apply(UUID uuid) {
                                        return MessagingServer.getInstance()
                                                .adventure()
                                                .player(uuid);
                                    }
                                })
                        .collect(Collectors.toSet()));
    }

    public MessagingChannel(EnumChannel channel, CommandSender... senders) {
        this(
                channel,
                Arrays.stream(senders)
                        .map(
                                new Function<CommandSender, Audience>() {
                                    @Override
                                    public Audience apply(CommandSender sender) {
                                        return MessagingServer.getInstance()
                                                .adventure()
                                                .sender(sender);
                                    }
                                })
                        .collect(Collectors.toSet()));
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

    public void addMember(Audience member) {
        this.members.add(member);
        this.audience = Audience.audience(this.members);
    }

    public void addMember(CommandSender sender) {
        this.addMember(this.server.adventure().sender(sender));
    }

    public void addMember(UUID player) {
        this.addMember(this.server.adventure().player(player));
    }

    public void removeMember(Audience audience) {
        this.members.remove(audience);
        this.audience = Audience.audience(this.members);
    }

    public void sendMessage(Message message) {
        this.getAudiences().sendMessage(message.asComponent());
        this.messageHistory.put(message, System.currentTimeMillis());
    }

    public void sendActionBar(Message message) {
        this.getAudiences().sendActionBar(message.asComponent());
    }

    public void sendActionBar(Message message, Duration duration, long delayMillis) {
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
        this.getAudiences().showBossBar(bossBar);
    }

    public void hideBossBar(BossBar bossBar) {
        this.getAudiences().hideBossBar(bossBar);
    }

    public Map<Message, Long> getMessageHistory() {
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
