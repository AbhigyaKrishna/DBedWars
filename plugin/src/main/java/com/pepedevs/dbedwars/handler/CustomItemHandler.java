package com.pepedevs.dbedwars.handler;

import com.pepedevs.corelib.utils.PluginHandler;
import com.pepedevs.corelib.item.ActionItemHandler;
import com.pepedevs.dbedwars.api.util.item.PluginActionItem;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomItemHandler extends PluginHandler
        implements com.pepedevs.dbedwars.api.handler.CustomItemHandler {

    private final Map<String, PluginActionItem> items;

    public CustomItemHandler(Plugin plugin) {
        super(plugin);
        this.items = new ConcurrentHashMap<>();
        this.register();
    }

    @Override
    protected boolean isAllowMultipleInstances() {
        return false;
    }

    @Override
    public synchronized void registerItem(String id, PluginActionItem item) {
        if (items.containsKey(id))
            throw new IllegalStateException(
                    "Custom item with id: `" + id + "` is already present!");

        this.items.put(id, item);
        ActionItemHandler.register(item);
    }

    @Override
    public synchronized void unregisterItem(String id) {
        PluginActionItem item = this.items.getOrDefault(id, null);
        if (item != null) {
            this.items.remove(id);
            ActionItemHandler.unregister(item);
        }
    }

    @Override
    public synchronized PluginActionItem getItem(String id) {
        return this.items.getOrDefault(id, null);
    }
}
