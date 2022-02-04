package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.ActionBarAction;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

public class ActionBarActionTranslator implements ActionTranslator<AbstractMessaging> {

    @Override
    public Action<AbstractMessaging> serialize(String untranslated) {
        return new ActionBarAction(Lang.getTranslator().asMessage(untranslated));
    }

    @Override
    public String deserialize(Action<AbstractMessaging> action) {
        return ((ActionBarAction) action).getMessage().getMessage();
    }

    @Override
    public Key<String> getKey() {
        return null;
    }

}
