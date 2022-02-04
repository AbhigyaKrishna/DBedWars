package com.pepedevs.dbedwars.api.action;

import com.pepedevs.dbedwars.api.util.Keyed;

public interface ActionTranslator<T, K extends Action<T>> extends Keyed<String> {

    K serialize(String untranslated, ActionPlaceholder<?, ?>... placeholders);

    String deserialize(K action);

}
