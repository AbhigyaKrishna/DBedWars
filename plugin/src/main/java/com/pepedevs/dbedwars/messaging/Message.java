package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.api.messaging.MessagingMember;

public class Message implements com.pepedevs.dbedwars.api.messaging.Message {

    private MessagingChannel channel;
    private MessagingMember sender;

    private String raw;

    public Message(MessagingChannel channel, MessagingMember sender){
        this.channel = channel;
        this.sender = sender;
    }

    public Message(MessagingChannel channel) {}
    public Message(String raw, MessagingChannel channel, MessagingMember sender){
        this.raw = raw;
        this.channel = channel;
        this.sender = sender;
    }

    public void send(){
        sender.sendMessage(this);
    }

    public MessagingChannel getChannel() {
        return channel;
    }

    public MessagingMember getSender() {
        return sender;
    }
}
