package org.zibble.dbedwars.script.action.translators;

import org.zibble.dbedwars.api.messaging.PlaceholderEntry;
import org.zibble.dbedwars.api.objects.serializable.TitleST;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.Key;
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
        TitleST titleST = TitleST.of(untranslated);
        if (titleST.getSubTitle() == null) {
            return new TitleAction(titleST.getTitle(), messaging);
        } else if (titleST.getFadeIn() == -1 || titleST.getStay() == -1 || titleST.getFadeOut() == -1) {
            return new TitleAction(titleST.getTitle(), titleST.getSubTitle(), messaging);
        } else {
            return new TitleAction(titleST.getTitle(), titleST.getSubTitle(), titleST.getFadeIn(), titleST.getStay(), titleST.getFadeOut(), messaging);
        }
    }

    @Override
    public String deserialize(TitleAction action) {
        return TitleST.of(action.getTitle(), action.getSubtitle(), (int) (action.getTimes().fadeIn().toMillis()/ 50), (int) (action.getTimes().stay().toMillis()/ 50), (int) (action.getTimes().fadeOut().toMillis()/ 50)).toString();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("TITLE");
    }

}
