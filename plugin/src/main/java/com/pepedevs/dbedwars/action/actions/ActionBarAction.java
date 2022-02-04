package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

public class ActionBarAction implements Action<AbstractMessaging> {

    private final Message message;

    public ActionBarAction(Message message) {
        this.message = message;
    }

    @Override
    public void execute(AbstractMessaging abstractMessaging) {
        abstractMessaging.sendActionBar(this.message);
    }

    public Message getMessage() {
        return this.message;
    }
}
