package com.pepedevs.dbedwars.messaging;

import net.kyori.adventure.bossbar.BossBar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MessagingChannel {

    private final Set<MessagingMember> channelMembers;
    private final MessagingHistory messagingHistory;
    private EnumChannel channelType;

    protected MessagingChannel() {
        this.channelMembers = new HashSet<>();
        this.messagingHistory = MessagingHistory.from(MessagingServer.connect().getHistory());
    }

    public void addMembers(MessagingMember... members) {
        this.channelMembers.addAll(Arrays.asList(members));
    }

    public void addConsole() {
        this.channelMembers.add(MessagingMember.ofConsole());
    }

    public void removeMembers(MessagingMember... members) {
        Arrays.asList(members).forEach(this.channelMembers::remove);
    }

    public boolean isMember(MessagingMember member) {
        return this.channelMembers.contains(member);
    }

    public void register() {
        MessagingServer.connect().registerChannels(this);
    }

    public void unregister() {
        MessagingServer.connect().unRegisterChannels(this);
    }

    public boolean isRegistered() {
        return MessagingServer.connect().registryCheck(this);
    }

    public SentMessage sendMessage(MessagingMember sender, Message message) {
        return MessagingServer.connect().sendMessage(message, sender, this);
    }

    public SentMessage sendToExcept(
            MessagingMember sender, Message message, MessagingMember... hiddenUsers) {
        return MessagingServer.connect().sendToExcept(message, sender, this, hiddenUsers);
    }

    public Set<MessagingMember> getChannelMemebers() {
        return new HashSet<>(channelMembers);
    }

    public EnumChannel getChannelType() {
        return channelType;
    }

    public void setChannelType(EnumChannel channelType) {
        this.channelType = channelType;
    }

    public MessagingHistory getMessagingHistory() {
        return messagingHistory;
    }
}
