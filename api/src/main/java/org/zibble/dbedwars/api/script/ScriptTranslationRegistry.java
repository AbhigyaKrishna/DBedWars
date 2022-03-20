package org.zibble.dbedwars.api.script;

import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.script.condition.ConditionTranslator;
import org.zibble.dbedwars.api.util.Key;

import java.util.Collection;

public interface ScriptTranslationRegistry {

    TranslationRegistry<? extends ActionTranslator<?, ? extends Action<?>>> actionRegistry();

    TranslationRegistry<? extends ConditionTranslator<?, ? extends Condition<?>>> conditionRegistry();

    interface TranslationRegistry<T extends Translator<? extends Translated>> {

        void registerTranslation(T translator);

        Collection<? extends Translator<? extends Translated>> getRegisteredTranslations();

        boolean isRegistered(Key<String> key);

        boolean isRegistered(String key);

        void unregisterTranslation(Key<String> key);

        void unregisterTranslation(String key);

        T getTranslator(Key<String> key);

        T getTranslator(String key);

    }

}
