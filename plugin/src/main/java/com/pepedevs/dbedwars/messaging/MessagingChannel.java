package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.messaging.member.MessagingMember;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MessagingChannel {

    private final Set<MessagingMember> channelMembers;
    private final EnumChannel channelType;

    public MessagingChannel(EnumChannel channelType) {
        this.channelMembers = new HashSet<>();
        this.channelType = channelType;
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

    public void sendMessage(MessagingMember sender, Message message) {
        MessagingServer.connect().sendMessage(message, sender, this);
    }

    public void sendToExcept(
            MessagingMember sender, Message message, MessagingMember... hiddenUsers) {
        MessagingServer.connect().sendToExcept(message, sender, this, hiddenUsers);
    }

    public Set<MessagingMember> getChannelMembers() {
        return new HashSet<>(channelMembers);
    }

    public EnumChannel getChannelType() {
        return channelType;
    }
}
