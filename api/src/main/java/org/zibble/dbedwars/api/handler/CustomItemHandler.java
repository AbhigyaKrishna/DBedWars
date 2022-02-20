package org.zibble.dbedwars.api.handler;

import org.zibble.dbedwars.api.util.item.BedWarsActionItem;

public interface CustomItemHandler {

    void registerItem(BedWarsActionItem item);

    void unregisterItem(String id);

    BedWarsActionItem getItem(String id);
}
