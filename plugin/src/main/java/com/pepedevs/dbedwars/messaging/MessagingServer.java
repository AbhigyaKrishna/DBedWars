package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.DBedwars;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MessagingServer {

    private static MessagingServer server;

    private final DBedwars plugin;
    private BukkitAudiences adventure;
    private Map<String, MessagingChannel> channels;

    public MessagingServer(DBedwars plugin) {
        server = this;
        this.plugin = plugin;
    }

    public void init() {
        this.adventure = BukkitAudiences.builder(this.plugin).build();
        this.channels = new ConcurrentHashMap<>();
    }

    public MessagingChannel registerChannel(String key, EnumChannel channel) {
        if (this.isRegistered(key))
            throw new IllegalStateException("Channel `" + key + "` already registered!");

        MessagingChannel messagingChannel = new MessagingChannel(key, channel, this);
        this.channels.put(key, messagingChannel);
        return messagingChannel;
    }

    public MessagingChannel unregisterChannel(String key) {
        if (!this.isRegistered(key))
            throw new IllegalStateException("No channel registration found with key `" + key + "`");

        return this.channels.remove(key);
    }

    public boolean isRegistered(String key) {
        return this.channels.containsKey(key);
    }

    public void broadcast(Message message) {
        this.adventure().players().sendMessage(message.asComponent());
    }

    public void broadcast(Message message, EnumChannel... channel) {
        Audience audience =
                Audience.audience(
                        this.channels.values().stream()
                                .filter(
                                        messagingChannel ->
                                                Arrays.stream(channel)
                                                        .anyMatch(
                                                                c ->
                                                                        messagingChannel
                                                                                        .getChannel()
                                                                                == c))
                                .map(MessagingChannel::getAudiences)
                                .collect(Collectors.toList()));
        audience.sendMessage(message.asComponent());
    }

    public void close() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException(
                    "Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    public DBedwars getPlugin() {
        return this.plugin;
    }

    public static MessagingServer get() {
        return server;
    }
}
