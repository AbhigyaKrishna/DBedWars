package com.pepedevs.dbedwars.api.handler;

import com.pepedevs.dbedwars.api.util.item.PluginActionItem;

public interface CustomItemHandler {

    void registerItem(String id, PluginActionItem item);

    void unregisterItem(String id);

    PluginActionItem getItem(String id);

}
