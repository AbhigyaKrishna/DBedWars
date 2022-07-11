package org.zibble.dbedwars.script.action.impl;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.messaging.AbstractMessaging;

import java.util.ArrayList;
import java.util.List;

public class ActionBarAction implements ActionTranslator<ActionBarAction.Action> {

    @Override
    public Action serialize(String untranslated, ScriptVariable<?>... variables) {
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
        return new Action(ConfigMessage.from(untranslated, entries.toArray(new PlaceholderEntry[0])), messaging);
    }

    @Override
    public String deserialize(Action action) {
        return action.getMessage().getMessage();
    }

    @Override
    public Key getKey() {
        return Key.of("ACTION_BAR");
    }

    public static class Action implements org.zibble.dbedwars.api.script.action.Action {

        private final AbstractMessaging messaging;
        private final Message message;

        public Action(Message message, AbstractMessaging abstractMessaging) {
            this.messaging = abstractMessaging;
            this.message = message;
        }

        @Override
        public void execute() {
            this.getMessaging().sendActionBar(this.getMessage());
        }

        public AbstractMessaging getMessaging() {
            return this.messaging;
        }

        public Message getMessage() {
            return this.message;
        }

    }

}
