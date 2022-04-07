package org.zibble.dbedwars.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.plugin.Plugin;
import org.zibble.dbedwars.api.plugin.PluginHandler;
import org.zibble.dbedwars.api.util.EventHandler;
import org.zibble.dbedwars.api.util.EventUtils;
import org.zibble.dbedwars.api.util.Initializable;
import org.zibble.dbedwars.api.util.item.ActionItem;
import org.zibble.dbedwars.api.util.item.BedWarsActionItem;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomItemHandler extends PluginHandler implements org.zibble.dbedwars.api.handler.CustomItemHandler, Initializable {

    private final Map<Key, BedWarsActionItem> items;
    private boolean initialized;

    public CustomItemHandler(Plugin plugin) {
        super(plugin);
        this.items = new ConcurrentHashMap<>();
        this.register();
    }

    public void init() {
        for (EventPriority priority : EventPriority.values()) {
            EventHandler.listen(this.getPlugin(), PlayerInteractEvent.class, priority, event -> {
                Player player = event.getPlayer();
                ItemStack item = event.getItem();

                if (item != null && event.getAction() != Action.PHYSICAL) {
                    ActionItem actionItem = null;
                    for (ActionItem value : items.values()) {
                        if (value.getPriority() != priority) continue;
                        if (!value.isThis(item)) continue;
                        actionItem = value;
                        break;
                    }

                    if (actionItem != null) {
                        ActionItem.EnumAction actionType;
                        boolean sneaking = player.isSneaking();
                        boolean sprinting = player.isSprinting();
                        boolean leftClick = EventUtils.isLeftClick(event.getAction());
                        boolean rightClick = EventUtils.isRightClick(event.getAction());

                        if (sneaking) {
                            actionType = leftClick ? ActionItem.EnumAction.LEFT_CLICK_SNEAKING :
                                    (rightClick ? ActionItem.EnumAction.RIGHT_CLICK_SNEAKING : null);
                        } else if (sprinting) {
                            actionType = leftClick ? ActionItem.EnumAction.LEFT_CLICK_SPRINTING :
                                    (rightClick ? ActionItem.EnumAction.RIGHT_CLICK_SPRINTING : null);
                        } else {
                            actionType = leftClick ? ActionItem.EnumAction.LEFT_CLICK :
                                    (rightClick ? ActionItem.EnumAction.RIGHT_CLICK : null);
                        }

                        if (actionType != null) {
                            actionItem.onActionPerform(event.getPlayer(), actionType, event);
                        } else {
                            throw new IllegalStateException("Couldn't determine performed action");
                        }
                    }
                }
            });
        }
        this.initialized = true;
    }

    @Override
    protected boolean isAllowMultipleInstances() {
        return false;
    }

    @Override
    public synchronized void registerItem(BedWarsActionItem item) {
        if (items.containsKey(item.getKey()))
            throw new IllegalStateException("Custom item with id: `" + item.getKey().get() + "` is already present!");

        this.items.put(item.getKey(), item);
    }

    @Override
    public synchronized void unregisterItem(String id) {
        this.items.remove(Key.of(id));
    }

    @Override
    public synchronized BedWarsActionItem getItem(String id) {
        return this.items.getOrDefault(Key.of(id), null);
    }

    @Override
    public synchronized BedWarsActionItem getItem(Key key) {
        return this.items.getOrDefault(key, null);
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

}
