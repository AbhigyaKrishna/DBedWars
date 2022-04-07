package org.zibble.dbedwars.api.script;

import org.zibble.dbedwars.api.util.key.Keyed;

public interface Translator<T> extends Keyed {

    T serialize(String condition, ScriptVariable<?>... variables);

    String deserialize(T condition);

}
