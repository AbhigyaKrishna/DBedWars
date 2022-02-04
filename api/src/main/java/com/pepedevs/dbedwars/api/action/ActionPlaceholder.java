package com.pepedevs.dbedwars.api.action;

import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.Keyed;

public interface ActionPlaceholder<K, R> extends Keyed<K> {

    String forTranslator();

    Key<K> getKey();

    R getPlaceholder();

}
