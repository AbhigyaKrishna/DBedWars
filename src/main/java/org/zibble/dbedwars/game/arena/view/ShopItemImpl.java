package org.zibble.dbedwars.game.arena.view;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.view.ItemTierGroup;
import org.zibble.dbedwars.api.game.view.ShopItem;
import org.zibble.dbedwars.api.game.view.ShopView;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.script.action.ActionPreProcessor;
import org.zibble.dbedwars.script.action.ActionProcessor;
import org.zibble.dbedwars.script.condition.ConditionPreProcessor;
import org.zibble.inventoryframework.ClickType;
import org.zibble.inventoryframework.MenuItem;
import org.zibble.inventoryframework.protocol.Item;

import java.util.HashSet;
import java.util.Set;

public class ShopItemImpl implements ShopItem {

    protected final ArenaPlayer player;
    protected final BwItemStack item;
    private final Set<Condition> useConditions;
    private final Set<ActionProcessor> actions;
    private ItemTierGroup tierGroup;

    public ShopItemImpl(ArenaPlayer player, BwItemStack item) {
        this.player = player;
        this.item = item;
        this.useConditions = new HashSet<>();
        this.actions = new HashSet<>();
    }

    ShopItemImpl(ArenaPlayer player, ShopInfoImpl.ItemInfoImpl item, Placeholder... placeholders) {
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

    @Override
    public ItemTierGroup getTierGroup() {
        return this.tierGroup;
    }

    @Override
    public void setTierGroup(ItemTierGroup tierGroup) {
        this.tierGroup = tierGroup;
    }

    @Override
    public boolean canUse() {
        for (Condition useCondition : this.useConditions) {
            if (!useCondition.test()) return false;
        }
        return true;
    }

    @Override
    public void use(ArenaPlayer player, ClickType clickType) {
        for (ActionProcessor action : this.actions) {
            action.execute();
        }
    }

    @Override
    public MenuItem<? extends Item> asMenuItem(ShopView shopView) {
        MenuItem<BwItemStack> menuItem = MenuItem.of(this.item);
        menuItem.setClickAction((protocolPlayer, clickType) -> {
            if (this.canUse()) {
                this.use(this.player, clickType);
                if (this.tierGroup != null) ((ShopViewImpl) shopView).getDataTracker().addTier(this.tierGroup);
            }
        });
        return menuItem;
    }

}
