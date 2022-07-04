package org.zibble.dbedwars.api.script.action;

import org.zibble.dbedwars.api.script.ScriptTranslationRegistry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.util.key.Key;

public interface ActionProvider {

    static ActionProvider fromString(String action) {
        return ScriptTranslationRegistry.get().actionProviderFromString(null, action);
    }

    static ActionProvider fromString(Key key, String action) {
        return ScriptTranslationRegistry.get().actionProviderFromString(key, action);
    }

    Action provide(ScriptVariable<?>... variables);

    default void execute(ScriptVariable<?>... variables) {
        this.provide(variables).execute();
    }

}
