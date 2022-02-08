package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

public class ActionBarAction implements Action<AbstractMessaging> {

    private final AbstractMessaging messaging;
    private final Message message;

    public ActionBarAction(Message message, AbstractMessaging abstractMessaging) {
        this.messaging = abstractMessaging;
        this.message = message;
    }

    @Override
    public void execute() {
        this.getHandle().sendActionBar(this.getMessage());
    }

    @Override
    public AbstractMessaging getHandle() {
        return this.messaging;
    }

    public Message getMessage() {
        return this.message;
    }
}
