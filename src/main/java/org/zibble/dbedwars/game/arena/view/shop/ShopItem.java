package org.zibble.dbedwars.game.arena.view.shop;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.messaging.Placeholder;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.util.NewBwItemStack;
import org.zibble.dbedwars.script.action.ActionPreProcessor;
import org.zibble.dbedwars.script.action.ActionProcessor;
import org.zibble.dbedwars.script.condition.ConditionPreProcessor;
import org.zibble.inventoryframework.ClickType;
import org.zibble.inventoryframework.MenuItem;
import org.zibble.inventoryframework.protocol.Item;
import org.zibble.inventoryframework.spigot.SpigotItem;

import java.util.HashSet;
import java.util.Set;

public class ShopItem {

    protected final ArenaPlayer player;
    protected final NewBwItemStack item;
    protected TierGroup tierGroup;
    private final Set<Condition<?>> useConditions;
    private final Set<ActionProcessor> actions;

    public ShopItem(ArenaPlayer player, NewBwItemStack item) {
        this.player = player;
        this.item = item;
        this.useConditions = new HashSet<>();
        this.actions = new HashSet<>();
    }

    ShopItem(ArenaPlayer player, ShopType.Item item, Placeholder... placeholders) {
        this.player = player;
        this.item = item.getItemFunction().apply(placeholders);
        this.useConditions = new HashSet<>();
        // TODO variables
        for (String useCondition : item.getUseConditions()) {
            this.useConditions.add(ConditionPreProcessor.process(useCondition));
        }
        this.actions = new HashSet<>();
        for (String action : item.getActions()) {
            this.actions.add(ActionPreProcessor.process(action));
        }
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
        MenuItem<SpigotItem> menuItem = MenuItem.of(new SpigotItem(this.item.asItemStack()));
        menuItem.setClickAction((protocolPlayer, clickType) -> {
            if (this.canUse()) {
                this.use(this.player, clickType);
            }
        });
        return menuItem;
    }
}
