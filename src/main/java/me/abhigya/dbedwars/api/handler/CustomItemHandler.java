package me.abhigya.dbedwars.api.handler;

import me.abhigya.dbedwars.api.util.item.PluginActionItem;

public interface CustomItemHandler {

    void registerItem(String id, PluginActionItem item);

    void unregisterItem(String id);

    PluginActionItem getItem(String id);
}
