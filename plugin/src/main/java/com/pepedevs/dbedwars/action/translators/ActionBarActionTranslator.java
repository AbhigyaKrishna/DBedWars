package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.ActionBarAction;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

import java.util.ArrayList;
import java.util.List;

public class ActionBarActionTranslator implements ActionTranslator<AbstractMessaging, ActionBarAction> {

    @Override
    public ActionBarAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        AbstractMessaging messaging = null;
        List<PlaceholderEntry> entries = new ArrayList<>();
        for (ActionPlaceholder<?, ?> placeholder : placeholders) {
            if (placeholder.getKey().equals("MESSAGING")) {
                messaging = (AbstractMessaging) placeholder.getPlaceholder();
            } else if (placeholder.getKey().equals("MESSAGE_PLACEHOLDER")) {
                entries.add((PlaceholderEntry) placeholder.getPlaceholder());
            }
        }
        return new ActionBarAction(Lang.getTranslator().asMessage(untranslated, entries.toArray(new PlaceholderEntry[0])), messaging);
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
