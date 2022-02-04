package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.TitleAction;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

public class TitleActionTranslator implements ActionTranslator<AbstractMessaging, TitleAction> {

    @Override
    public TitleAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        return null;
    }

    @Override
    public String deserialize(TitleAction action) {
        return null;
    }

    @Override
    public Key<String> getKey() {
        return Key.of("TITLE");
    }

}
