package org.zibble.dbedwars.game.arena.view.shop;

import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.util.NewBwItemStack;
import org.zibble.dbedwars.script.action.ActionProcessor;
import org.zibble.dbedwars.script.condition.ConditionProcessor;
import org.zibble.inventoryframework.ClickType;
import org.zibble.inventoryframework.MenuItem;
import org.zibble.inventoryframework.protocol.item.StackItem;
import org.zibble.inventoryframework.spigot.SpigotItem;

import java.util.HashSet;
import java.util.Set;

public class ShopItem {

    protected final ItemStack item;
    protected TierGroup tierGroup;
    private final Set<ConditionProcessor<?>> useConditions;
    private final Set<ActionProcessor> actions;

    public ShopItem(ItemStack item) {
        this.item = item;
        this.useConditions = new HashSet<>();
        this.actions = new HashSet<>();
    }

    public TierGroup getTierGroup() {
        return this.tierGroup;
    }

    public void setTierGroup(TierGroup tierGroup) {
        this.tierGroup = tierGroup;
    }

    public StackItem getGuiIcon() {
        return null;
    }

    public boolean canUse(ArenaPlayer arenaPlayer) {
        for (ConditionProcessor<?> useCondition : this.useConditions) {
            if (!useCondition.test()) return false;
        }
        return true;
    }

    public void use(ArenaPlayer player, ClickType clickType) {
        for (ActionProcessor action : this.actions) {
            action.execute();
        }
    }

    public MenuItem<SpigotItem> asMenuItem() {
        MenuItem<SpigotItem> menuItem = MenuItem.of(new NewBwItemStack(this.item).asStackItem());
        menuItem.setClickAction((protocolPlayer, clickType) -> {
            if (this.canUse(protocolPlayer.getArenaPlayer())) {
                this.use(protocolPlayer.getArenaPlayer(), clickType);
            }
        });
    }
}
