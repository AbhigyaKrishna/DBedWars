package com.pepedevs.dbedwars.messaging;

import java.util.Arrays;
import java.util.Set;

public abstract class MessagingChannel {

    private Set<MessagingMember> channelMembers;
    private EnumChannel channelType;

    public void addMember(MessagingMember member){
        this.channelMembers.add(member);
    }

    public void addMembers(MessagingMember... members){
        this.channelMembers.addAll(Arrays.asList(members));
    }

    public void removeMember(MessagingMember member){
        this.channelMembers.removeIf(member::equals);
    }

    public void removeMembers(MessagingMember... members){
        for (MessagingMember member : members) {
            this.removeMember(member);
        }
    }

    public void register(){
        MessagingServer.connect().registerChannel(this);
    }

    public boolean isRegistered(){
        return MessagingServer.connect().registryCheck(this);
    }

    public SentMessage sendMessage(MessagingMember member, Message message){
        return MessagingServer.connect().sendMessage(message, member, this);
    }

    public Set<MessagingMember> getChannelMemebers() {
        return channelMembers;
    }

    public EnumChannel getChannelType() {
        return channelType;
    }

    public void setChannelType(EnumChannel channelType) {
        this.channelType = channelType;
    }
}
