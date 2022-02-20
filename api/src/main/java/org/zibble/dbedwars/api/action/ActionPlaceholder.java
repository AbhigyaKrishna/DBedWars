package org.zibble.dbedwars.api.action;

import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.Keyed;

public interface ActionPlaceholder<K, V> extends Keyed<K> {

    Key<K> getKey();

    V getValue();

}
