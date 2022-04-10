package org.zibble.dbedwars.api.script;

import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.script.condition.ConditionTranslator;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.Collection;

public interface ScriptTranslationRegistry {

    Key GLOBAL = Key.of("global");

    default TranslationRegistry<? extends ActionTranslator<?, ? extends Action<?>>> actionRegistry() {
        return actionRegistry(GLOBAL);
    }

    TranslationRegistry<? extends ActionTranslator<?, ? extends Action<?>>> actionRegistry(Key key);

    default TranslationRegistry<? extends ConditionTranslator<?, ? extends Condition<?>>> conditionRegistry() {
        return conditionRegistry(GLOBAL);
    }

    TranslationRegistry<? extends ConditionTranslator<?, ? extends Condition<?>>> conditionRegistry(Key key);

    interface TranslationRegistry<T extends Translator<? extends Translated>> {

        void registerTranslation(T translator);

        Collection<? extends Translator<? extends Translated>> getRegisteredTranslations();

        boolean isRegistered(Key key);

        boolean isRegistered(String key);

        void unregisterTranslation(Key key);

        void unregisterTranslation(String key);

        T getTranslator(Key key);

        T getTranslator(String key);

    }

}
