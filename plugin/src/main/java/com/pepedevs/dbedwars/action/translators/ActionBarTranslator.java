package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

public class ActionBarTranslator implements ActionTranslator<AbstractMessaging> {

    @Override
    public Action<AbstractMessaging> serialize(String untranslated) {
        return null;
    }

    @Override
    public String deserialize(Action<AbstractMessaging> action) {
        return null;
    }

    @Override
    public Key<String> getKey() {
        return null;
    }

}
