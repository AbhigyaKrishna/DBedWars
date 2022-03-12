package org.zibble.dbedwars.script.action.impl;

import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.messaging.AbstractMessaging;

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
