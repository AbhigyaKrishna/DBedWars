package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.api.messaging.Message;

public class MessagingServer {

    private static MessagingServer server;

    protected void sendMessage(Message message){

    }

    public static MessagingServer connect(){
        return server;
    }

}
