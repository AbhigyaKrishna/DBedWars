package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.ActionBarAction;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

public class ActionBarActionTranslator implements ActionTranslator<AbstractMessaging, ActionBarAction> {

    @Override
    public ActionBarAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        return new ActionBarAction(Lang.getTranslator().asMessage(untranslated));
    }

    @Override
    public String deserialize(ActionBarAction action) {
        return action.getMessage().getMessage();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("ACTION_BAR");
    }

}
