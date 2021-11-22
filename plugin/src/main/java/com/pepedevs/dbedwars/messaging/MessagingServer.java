package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.DBedwars;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public class MessagingServer {

    private static MessagingServer server;

    private final DBedwars plugin;
    private BukkitAudiences adventure;

    public MessagingServer(DBedwars plugin) {
        server = this;
        this.plugin = plugin;
    }

    public void init() {
        this.adventure = BukkitAudiences.builder(this.plugin).build();
    }

    public void broadcast(Message message) {

    }

    public void close() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    public DBedwars getPlugin() {
        return this.plugin;
    }

    public static MessagingServer getInstance() {
        return server;
    }
}
