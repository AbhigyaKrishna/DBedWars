package org.zibble.dbedwars.action.translators;

import org.zibble.dbedwars.action.actions.SendMessageAction;
import org.zibble.dbedwars.api.action.ActionPlaceholder;
import org.zibble.dbedwars.api.action.ActionTranslator;
import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.messaging.AbstractMessaging;

import java.util.ArrayList;
import java.util.List;

public class SendMessageActionTranslator implements ActionTranslator<AbstractMessaging, SendMessageAction> {
    @Override
    public SendMessageAction serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders) {
        AbstractMessaging messaging = null;
        final List<PlaceholderEntry> entries = new ArrayList<>();
        for (ActionPlaceholder<?, ?> placeholder : placeholders) {
            if (placeholder.getValue() instanceof AbstractMessaging) {
                messaging = (AbstractMessaging) placeholder.getValue();
            }
            if (placeholder.getKey().equals("PLACEHOLDER")) {
                entries.add((PlaceholderEntry) placeholder.getValue());
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
