package com.pepedevs.dbedwars.api.action;

import com.pepedevs.dbedwars.api.util.Keyed;

public interface ActionTranslator <T>  extends Keyed<String> {

    Action<T> serialize(String untranslated);

    String deserialize(Action<T> action);

}
