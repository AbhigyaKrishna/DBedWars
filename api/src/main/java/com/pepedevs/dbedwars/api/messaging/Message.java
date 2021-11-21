package com.pepedevs.dbedwars.api.messaging;

public interface Message {

    void send();

    MessagingChannel getChannel();

    MessagingMember getSender();

}
