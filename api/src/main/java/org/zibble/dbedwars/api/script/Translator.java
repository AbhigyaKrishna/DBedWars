package org.zibble.dbedwars.api.script;

import org.zibble.dbedwars.api.util.Keyed;

public interface Translator<T> extends Keyed<String> {

    T serialize(String condition, ScriptVariable<?>... variables);

    String deserialize(T condition);

}
