package org.zibble.dbedwars.api.script.action;

import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.Translator;

public interface ActionTranslator<T, K extends Action<T>> extends Translator<K> {

    K serialize(String untranslated, ScriptVariable<?>... variables);

    String deserialize(K action);

}
