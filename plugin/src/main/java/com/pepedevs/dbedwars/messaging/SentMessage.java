package com.pepedevs.dbedwars.messaging;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SentMessage extends Message{

    private final Message rawMessage;
    private final MessagingChannel messagingChannel;
    private final MessagingMember sender;
    private final Long timestamp;
    private final Set<MessagingMember> receivers;

    protected SentMessage(Message message, MessagingChannel channel, MessagingMember member) {
        this(message, channel, member, System.currentTimeMillis());
    }

    protected SentMessage(Message message, MessagingChannel channel, MessagingMember member, Long timestamp) {
        super();
        this.rawMessage = message;
        this.messagingChannel = channel;
        this.sender = member;
        this.timestamp = timestamp;
        this.receivers = new HashSet<>(channel.getChannelMemebers());
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

    public Long getTimestamp() {
        return this.timestamp;
    }

    public Instant getSentTime() {
        return Instant.ofEpochMilli(this.timestamp);
    }
}
