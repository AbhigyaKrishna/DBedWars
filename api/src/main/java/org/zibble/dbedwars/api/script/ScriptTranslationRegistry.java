package org.zibble.dbedwars.api.script;

import org.zibble.dbedwars.api.script.action.Action;
import org.zibble.dbedwars.api.script.action.ActionProvider;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.script.condition.ConditionProvider;
import org.zibble.dbedwars.api.script.condition.ConditionTranslator;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.Collection;

public abstract class ScriptTranslationRegistry {

    private static ScriptTranslationRegistry instance;
    public static final Key GLOBAL = Key.of("global");

    public ScriptTranslationRegistry() {
        instance = this;
    }

    public static ScriptTranslationRegistry get() {
        return instance;
    }

    public TranslationRegistry<? extends ActionTranslator<? extends Action>> actionRegistry() {
        return actionRegistry(GLOBAL);
    }

    public abstract TranslationRegistry<? extends ActionTranslator<? extends Action>> actionRegistry(Key key);

    public TranslationRegistry<? extends ConditionTranslator<? extends Condition>> conditionRegistry() {
        return conditionRegistry(GLOBAL);
    }

    public abstract TranslationRegistry<? extends ConditionTranslator<? extends Condition>> conditionRegistry(Key key);

    public abstract ActionProvider actionProviderFromString(Key key, String action);

    public abstract ConditionProvider conditionProviderFromString(Key key, String condition);

    public interface TranslationRegistry<T extends Translator<? extends Translated>> {

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
