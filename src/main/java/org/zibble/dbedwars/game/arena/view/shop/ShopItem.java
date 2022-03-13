package org.zibble.dbedwars.game.arena.view.shop;

import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.inventoryframework.ClickType;
import org.zibble.inventoryframework.protocol.item.StackItem;

import java.util.HashSet;
import java.util.Set;

public abstract class ShopItem {

    protected final ItemStack item;
    protected TierGroup tierGroup;
    protected final Set<Condition<?>> useConditions;

    public ShopItem(ItemStack item) {
        this.item = item;
        this.useConditions = new HashSet<>();
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
        for (Condition<?> useCondition : this.useConditions) {
            if (!useCondition.test(arenaPlayer)) return false;
        }
        return true;
    }

    public abstract void use(ArenaPlayer player, ClickType clickType);

}
