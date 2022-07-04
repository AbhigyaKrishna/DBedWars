package org.zibble.dbedwars.script.action.impl;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.messaging.AbstractMessaging;

public class ActionBarAction implements Action {

    private final AbstractMessaging messaging;
    private final Message message;

    public ActionBarAction(Message message, AbstractMessaging abstractMessaging) {
        this.messaging = abstractMessaging;
        this.message = message;
    }

    @Override
    public void execute() {
        this.getMessaging().sendActionBar(this.getMessage());
    }

    public AbstractMessaging getMessaging() {
        return this.messaging;
    }

    public Message getMessage() {
        return this.message;
    }

}
