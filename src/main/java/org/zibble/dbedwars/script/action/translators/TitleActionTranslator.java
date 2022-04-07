package org.zibble.dbedwars.script.action.translators;

import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.TitleST;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.messaging.AbstractMessaging;
import org.zibble.dbedwars.script.action.impl.TitleAction;

public class TitleActionTranslator implements ActionTranslator<AbstractMessaging, TitleAction> {

    @Override
    public TitleAction serialize(String untranslated, ScriptVariable<?>... variables) {
        AbstractMessaging messaging = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isAssignableFrom(AbstractMessaging.class)) {
                messaging = (AbstractMessaging) variable.value();
            } else if (variable.getKey().equals("PLACEHOLDER")) {
                untranslated = ((PlaceholderEntry) variable.value()).apply(untranslated);
            }
        }
        TitleST title = TitleST.valueOf(untranslated.trim());
        return new TitleAction(title, messaging);
    }

    @Override
    public String deserialize(TitleAction action) {
        return action.getTitle().toString();
    }

    @Override
    public Key getKey() {
        return Key.of("TITLE");
    }

}
