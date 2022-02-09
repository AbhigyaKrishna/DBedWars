package com.pepedevs.dbedwars.api.handler;

import com.pepedevs.dbedwars.api.util.item.BedWarsActionItem;

public interface CustomItemHandler {

    void registerItem(BedWarsActionItem item);

    void unregisterItem(String id);

    BedWarsActionItem getItem(String id);
}
