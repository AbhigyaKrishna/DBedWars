package org.zibble.dbedwars.api.script.condition;

import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.Translator;

public interface ConditionTranslator<T, K extends Condition<T>> extends Translator<K> {

    K serialize(String condition, ScriptVariable<?>... variables);

    String deserialize(K condition);

}
