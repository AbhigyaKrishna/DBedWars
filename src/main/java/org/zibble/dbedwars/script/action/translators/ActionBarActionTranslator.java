package org.zibble.dbedwars.script.action.translators;

import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.messaging.AbstractMessaging;
import org.zibble.dbedwars.script.action.impl.ActionBarAction;

import java.util.ArrayList;
import java.util.List;

public class ActionBarActionTranslator implements ActionTranslator<AbstractMessaging, ActionBarAction> {

    @Override
    public ActionBarAction serialize(String untranslated, ScriptVariable<?>... variables) {
        AbstractMessaging messaging = null;
        final List<PlaceholderEntry> entries = new ArrayList<>();
        for (ScriptVariable<?> variable : variables) {
            if (variable.isNull()) continue;
            if (variable.isAssignableFrom(AbstractMessaging.class)) {
                messaging = (AbstractMessaging) variable.value();
            } else if (variable.getKey().get().equals("MESSAGE_PLACEHOLDER")) {
                entries.add((PlaceholderEntry) variable.value());
            } else if (variable.getKey().get().equals("PLACEHOLDER")) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
            }
        }
        return new ActionBarAction(ConfigMessage.from(untranslated, entries.toArray(new PlaceholderEntry[0])), messaging);
    }

    @Override
    public String deserialize(ActionBarAction action) {
        return action.getMessage().getMessage();
    }

    @Override
    public Key getKey() {
        return Key.of("ACTION_BAR");
    }

}
