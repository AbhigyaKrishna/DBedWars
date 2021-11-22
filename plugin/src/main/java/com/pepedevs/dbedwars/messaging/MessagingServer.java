package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.DBedwars;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

import java.util.HashSet;
import java.util.Set;

public class MessagingServer {

    private final DBedwars plugin;
    private BukkitAudiences adventure;

    private static MessagingServer server;

    private Set<MessagingChannel> registeredChannels;

    public MessagingServer(DBedwars plugin) {
        server = this;
        this.plugin = plugin;
    }

    public void start() {
        this.adventure = BukkitAudiences.builder(plugin).build();
        this.registeredChannels = new HashSet<>();
    }

    protected SentMessage sendMessage(Message message, MessagingMember sender, MessagingChannel channel){
        return new SentMessage(message, channel, sender);
    }

    protected void registerChannel(MessagingChannel channel){

    }

    protected void registerChannels(MessagingChannel... channels){

    }

    protected Set<MessagingChannel> getRegisteredChannels() {
        return registeredChannels;
    }

    protected static MessagingServer connect(){
        return server;
    }

    protected boolean registryCheck(MessagingChannel channel){
        return this.getRegisteredChannels().contains(channel);
    }

}
