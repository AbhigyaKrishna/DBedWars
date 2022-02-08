package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

public class SendMessageAction implements Action<AbstractMessaging> {

    private final AbstractMessaging messaging;
    private final Message message;

    public SendMessageAction(AbstractMessaging messaging, Message message) {
        this.messaging = messaging;
        this.message = message;
    }

    @Override
    public void execute() {
        this.messaging.sendMessage(this.message);
    }

    @Override
    public AbstractMessaging getHandle() {
        return this.messaging;
    }

    public Message getMessage() {
        return message;
    }
}
