package com.pepedevs.dbedwars.messaging;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class SentMessage extends Message {

    private final Message rawMessage;
    private final MessagingChannel messagingChannel;
    private final MessagingMember sender;
    private final Long timestamp;
    private final Set<MessagingMember> receivers;
    private final Set<MessagingMember> hiddenUsers;

    protected SentMessage(Message message, MessagingChannel channel, MessagingMember sender) {
        this(message, channel, sender, System.currentTimeMillis(), channel.getChannelMemebers());
    }

    protected SentMessage(
            Message message, MessagingChannel channel, MessagingMember sender, Long timestamp) {
        this(message, channel, sender, timestamp, channel.getChannelMemebers());
    }

    protected SentMessage(
            Message message,
            MessagingChannel channel,
            MessagingMember sender,
            Long timestamp,
            MessagingMember hiddenUser) {
        this(message, channel, sender, timestamp, channel.getChannelMemebers());
    }

    protected SentMessage(
            Message message,
            MessagingChannel channel,
            MessagingMember sender,
            Long timestamp,
            Set<MessagingMember> receivers) {
        super();
        this.rawMessage = message;
        this.messagingChannel = channel;
        this.sender = sender;
        this.timestamp = timestamp;
        this.receivers = new HashSet<>(receivers);
        this.hiddenUsers = new HashSet<>(this.messagingChannel.getChannelMemebers());
        this.hiddenUsers.removeAll(receivers);
    }

    @Override
    public SentMessage send(MessagingMember member, MessagingChannel channel) {
        throw new IllegalStateException("Cannot send an already sent message");
    }

    public Message getRawMessage() {
        return rawMessage;
    }

    public MessagingChannel getSentChannel() {
        return this.messagingChannel;
    }

    public MessagingMember getSender() {
        return this.sender;
    }

    public Set<MessagingMember> getReceivers() {
        return new HashSet<>(this.receivers);
    }

    public Set<MessagingMember> getHiddenMembers() {
        return new HashSet<>(this.hiddenUsers);
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public Instant getSentTime() {
        return Instant.ofEpochMilli(this.timestamp);
    }

    public SentMessage sendAgain() {
        return this.rawMessage
                .clone()
                .sendToExcept(
                        sender, messagingChannel, hiddenUsers.toArray(new MessagingMember[0]));
    }

    public SentMessage sendAgainIn(MessagingChannel channel) {
        return this.rawMessage.clone().send(sender, channel);
    }

}
