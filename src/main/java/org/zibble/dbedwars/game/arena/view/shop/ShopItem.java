package org.zibble.dbedwars.game.arena.view.shop;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.util.NewBwItemStack;
import org.zibble.dbedwars.script.action.ActionProcessor;
import org.zibble.inventoryframework.ClickType;
import org.zibble.inventoryframework.MenuItem;
import org.zibble.inventoryframework.protocol.Item;

import java.util.HashSet;
import java.util.Set;

public class ShopItem {

    protected final NewBwItemStack item;
    protected TierGroup tierGroup;
    private final Set<Condition<?>> useConditions;
    private final Set<ActionProcessor> actions;

    public ShopItem(NewBwItemStack item) {
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

    public boolean canUse() {
        for (Condition<?> useCondition : this.useConditions) {
            if (!useCondition.test()) return false;
        }
        return true;
    }

    public void use(ArenaPlayer player, ClickType clickType) {
        for (ActionProcessor action : this.actions) {
            action.execute();
        }
    }

    public MenuItem<? extends Item> asMenuItem() {
        MenuItem<NewBwItemStack> menuItem = MenuItem.of(this.item);
        menuItem.setClickAction((protocolPlayer, clickType) -> {
            if (this.canUse(protocolPlayer.getArenaPlayer())) {
                this.use(protocolPlayer.getArenaPlayer(), clickType);
            }
        });
        return menuItem;
    }
}
