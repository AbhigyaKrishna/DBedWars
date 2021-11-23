package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.DBedwars;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MessagingServer {

    private final DBedwars plugin;
    private BukkitAudiences adventure;

    private Set<MessagingMember> registeredMembers;
    private Set<MessagingChannel> registeredChannels;
    private MessagingMember consoleMessagingMember;
    private MessagingChannel consoleLogger;

    private static MessagingServer server;

    public MessagingServer(DBedwars plugin) {
        server = this;
        this.plugin = plugin;
    }

    public void start(ConsoleCommandSender console) {
        this.adventure = BukkitAudiences.builder(plugin).build();
        this.registeredChannels = new HashSet<>();
        this.registeredMembers = new HashSet<>();

        this.consoleMessagingMember = new MessagingMember(adventure.sender(console), console);
    }

    protected SentMessage sendMessage(Message message, MessagingMember sender, MessagingChannel channel){
        return new SentMessage(message, channel, sender);
    }

    protected SentMessage sendToExcept(Message message, MessagingMember sender, MessagingChannel channel, MessagingMember hiddenUser) {
        return new SentMessage(message, channel, sender, System.currentTimeMillis(), hiddenUser);
    }

    protected SentMessage sendToExcept(Message message, MessagingMember sender, MessagingChannel channel, MessagingMember... hiddenUsers) {
        Set<MessagingMember> receivers = channel.getChannelMemebers();
        receivers.removeAll(Arrays.asList(hiddenUsers));
        return new SentMessage(message, channel, sender, System.currentTimeMillis(), receivers);
    }

    protected SentMessage sendToConsole(Message message) {
        return new SentMessage(message, consoleLogger, consoleMessagingMember);
    }

    protected void registerChannel(MessagingChannel channel){
        this.registeredChannels.add(channel);
    }

    protected void registerChannels(MessagingChannel... channels){
        this.registeredChannels.addAll(Arrays.asList(channels));
    }

    protected void unRegisterChannel(MessagingChannel channel) {
        this.registeredChannels.remove(channel);
    }

    protected void unRegisterChannels(MessagingChannel... channels) {
        this.registeredChannels.removeAll(Arrays.asList(channels));
    }

    protected Set<MessagingChannel> getRegisteredChannels() {
        return registeredChannels;
    }

    protected static MessagingServer connect(){
        return server;
    }

    protected MessagingMember getMessagingMember(Player player) {
        for (MessagingMember member : this.registeredMembers) {
            if (!member.isPlayer()) continue;
            if (member.getAsPlayer().equals(player)) return member;
        }

        MessagingMember member = new MessagingMember(adventure.player(player), player);
        this.registeredMembers.add(member);
        return member;
    }

    protected MessagingMember getConsole() {
        return this.consoleMessagingMember;
    }

    protected boolean registryCheck(MessagingChannel channel){
        return this.getRegisteredChannels().contains(channel);
    }

}
