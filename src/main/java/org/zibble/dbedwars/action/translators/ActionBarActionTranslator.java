package org.zibble.dbedwars.action.translators;

import org.zibble.dbedwars.action.actions.ActionBarAction;
import org.zibble.dbedwars.api.action.ActionPlaceholder;
import org.zibble.dbedwars.api.action.ActionTranslator;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.configuration.Lang;
import org.zibble.dbedwars.messaging.AbstractMessaging;

import java.util.ArrayList;
import java.util.List;

public class ActionBarActionTranslator implements ActionTranslator<AbstractMessaging, ActionBarAction> {

    @Override
    public ActionBarAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        AbstractMessaging messaging = null;
        final List<PlaceholderEntry> entries = new ArrayList<>();
        for (ActionPlaceholder<?, ?> placeholder : placeholders) {
            if (placeholder.getValue() instanceof AbstractMessaging) {
                messaging = (AbstractMessaging) placeholder.getValue();
            } else if (placeholder.getKey().equals("MESSAGE_PLACEHOLDER")) {
                entries.add((PlaceholderEntry) placeholder.getValue());
            } else if (placeholder.getKey().equals("PLACEHOLDER")) {
                untranslated = ((PlaceholderEntry) placeholder.getValue()).apply(untranslated);
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
