package org.zibble.dbedwars.script.action.impl;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.messaging.AbstractMessaging;

import java.util.ArrayList;
import java.util.List;

public class SendMessageAction implements ActionTranslator<SendMessageAction.Action> {

    @Override
    public Action serialize(String untranslated, ScriptVariable<?>... variables) {
        AbstractMessaging messaging = null;
        final List<PlaceholderEntry> entries = new ArrayList<>();
        for (ScriptVariable<?> variable : variables) {
            if (variable.isSubClassOf(AbstractMessaging.class)) {
                messaging = (AbstractMessaging) variable.value();
            } else if (variable.getKey().equals("PLACEHOLDER")) {
                entries.add((PlaceholderEntry) variable.value());
            }
        }
        return new Action(messaging, ConfigLang.getTranslator().asMessage(untranslated, entries.toArray(new PlaceholderEntry[0])));
    }

    @Override
    public String deserialize(Action action) {
        return action.getMessage().getMessage();
    }

    @Override
    public Key getKey() {
        return Key.of("SEND_MESSAGE");
    }

    public static class Action implements org.zibble.dbedwars.api.script.action.Action {

        private final AbstractMessaging messaging;
        private final Message message;

        public Action(AbstractMessaging messaging, Message message) {
            this.messaging = messaging;
            this.message = message;
        }

        @Override
        public void execute() {
            this.messaging.sendMessage(this.message);
        }

        public AbstractMessaging getMessaging() {
            return this.messaging;
        }

        public Message getMessage() {
            return message;
        }

    }

}
