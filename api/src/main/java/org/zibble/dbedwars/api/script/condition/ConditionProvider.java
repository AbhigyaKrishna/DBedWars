package org.zibble.dbedwars.api.script.condition;

import org.zibble.dbedwars.api.script.ScriptTranslationRegistry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.util.key.Key;

public interface ConditionProvider {

    static ConditionProvider fromString(String condition) {
        return ScriptTranslationRegistry.get().conditionProviderFromString(null, condition);
    }

    static ConditionProvider fromString(Key key, String condition) {
        return ScriptTranslationRegistry.get().conditionProviderFromString(key, condition);
    }

    Condition provide(ScriptVariable<?>... variables);

    default boolean test(ScriptVariable<?>... variables) {
        return this.provide(variables).test();
    }

}
