package com.pepedevs.dbedwars.messaging;

public class MessagingMember implements com.pepedevs.dbedwars.api.messaging.MessagingMember {

    @Override
    public void sendMessage(com.pepedevs.dbedwars.api.messaging.Message message) {
        message.getChannel().sendMessage(message);
    }

    

}
