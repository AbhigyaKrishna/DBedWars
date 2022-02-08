package com.pepedevs.dbedwars.action.translators;

import com.pepedevs.dbedwars.action.actions.SendMessageAction;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.action.ActionTranslator;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;

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
        return new SendMessageAction(messaging, Lang.getTranslator().asMessage(untranslated, entries.toArray(new PlaceholderEntry[0])));
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
