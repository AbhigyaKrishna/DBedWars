package org.zibble.dbedwars.script.action.translators;

import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.messaging.AbstractMessaging;
import org.zibble.dbedwars.script.action.impl.SendMessageAction;

import java.util.ArrayList;
import java.util.List;

public class SendMessageActionTranslator implements ActionTranslator<AbstractMessaging, SendMessageAction> {
    @Override
    public SendMessageAction serialize(String untranslated, ScriptVariable<?>... variables) {
        AbstractMessaging messaging = null;
        final List<PlaceholderEntry> entries = new ArrayList<>();
        for (ScriptVariable<?> variable : variables) {
            if (variable.isAssignableFrom(AbstractMessaging.class)) {
                messaging = (AbstractMessaging) variable.value();
            } else if (variable.getKey().equals("PLACEHOLDER")) {
                entries.add((PlaceholderEntry) variable.value());
            }
        }
        return new SendMessageAction(messaging, ConfigLang.getTranslator().asMessage(untranslated, entries.toArray(new PlaceholderEntry[0])));
    }

    @Override
    public String deserialize(SendMessageAction action) {
        return action.getMessage().getMessage();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("SEND_MESSAGE");
    }
}
