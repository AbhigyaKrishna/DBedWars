package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

public class ActionBarAction implements Action<AbstractMessaging> {

    private final String line;

    public ActionBarAction(String line) {
        this.line = line;
    }

    @Override
    public void execute(AbstractMessaging abstractMessaging) {
        abstractMessaging.sendActionBar(Lang.getTranslator().asMessage(line));
    }

    public String getMessage() {
        return line;
    }
}
