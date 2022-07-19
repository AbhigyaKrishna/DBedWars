package org.zibble.dbedwars.script.action.impl;

import org.zibble.dbedwars.api.messaging.AbstractMessaging;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.TitleST;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;

public class TitleAction implements ActionTranslator<TitleAction.Action> {

    @Override
    public Action serialize(String untranslated, ScriptVariable<?>... variables) {
        AbstractMessaging messaging = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isSubClassOf(AbstractMessaging.class)) {
                messaging = (AbstractMessaging) variable.value();
            } else if (variable.getKey().equals("PLACEHOLDER")) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
            }
        }
        TitleST title = TitleST.valueOf(untranslated.trim());
        return new Action(title, messaging);
    }

    @Override
    public String deserialize(Action action) {
        return action.getTitle().toString();
    }

    @Override
    public Key getKey() {
        return Key.of("TITLE");
    }

    public static class Action implements org.zibble.dbedwars.api.script.action.Action {

        private final TitleST title;
        private final AbstractMessaging abstractMessaging;

        public Action(TitleST title, AbstractMessaging abstractMessaging) {
            this.title = title;
            this.abstractMessaging = abstractMessaging;
        }

        @Override
        public void execute() {
            this.title.send(this.abstractMessaging);
        }

        public AbstractMessaging getMessaging() {
            return this.abstractMessaging;
        }

        public TitleST getTitle() {
            return title;
        }

    }

}
