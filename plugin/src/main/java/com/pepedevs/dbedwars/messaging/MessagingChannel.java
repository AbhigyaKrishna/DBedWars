package com.pepedevs.dbedwars.messaging;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class MessagingChannel {

    private Set<MessagingMember> channelMembers;
    private EnumChannel channelType;

    public void addMember(MessagingMember member) {
        this.channelMembers.add(member);
    }

    public void addMembers(MessagingMember... members) {
        this.channelMembers.addAll(Arrays.asList(members));
    }

    public void addConsole() {
        this.channelMembers.add(MessagingMember.ofConsole());
    }

    public void removeMember(MessagingMember member) {
        this.channelMembers.removeIf(member::equals);
    }

    public void removeMembers(MessagingMember... members) {
        for (MessagingMember member : members) {
            this.removeMember(member);
        }
    }

    public boolean isMember(MessagingMember member) {
        return this.channelMembers.contains(member);
    }

    public void register() {
        MessagingServer.connect().registerChannel(this);
    }

    public void unregister() {
        MessagingServer.connect().unRegisterChannel(this);
    }

    public boolean isRegistered() {
        return MessagingServer.connect().registryCheck(this);
    }

    public SentMessage sendMessage(MessagingMember sender, Message message) {
        return MessagingServer.connect().sendMessage(message, sender, this);
    }

    public SentMessage sendToExcept(
            MessagingMember sender, Message message, MessagingMember hiddenUser) {
        return MessagingServer.connect().sendToExcept(message, sender, this, hiddenUser);
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
}
