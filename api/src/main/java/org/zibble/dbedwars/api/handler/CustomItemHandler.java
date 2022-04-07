package org.zibble.dbedwars.api.handler;

import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.api.util.key.Key;

public interface CustomItemHandler {

    void registerItem(BedWarsActionItem item);

    void unregisterItem(String id);

    BedWarsActionItem getItem(String id);

    BedWarsActionItem getItem(Key key);

}
