package org.zibble.dbedwars.api.events;


import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.view.ShopItem;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class PlayerPurchaseItemEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private ArenaPlayer player;
    private Arena arena;
    private Set<ItemStack> cost;
    private Collection<ShopItem> items;

    public PlayerPurchaseItemEvent(
            ArenaPlayer player, Arena arena, Set<ItemStack> cost, Collection<ShopItem> items) {
        this.player = player;
        this.arena = arena;
        this.cost = cost;
        this.items = items;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public ArenaPlayer getPlayer() {
        return this.player;
    }

    public Arena getArena() {
        return this.arena;
    }

    public Set<ItemStack> getCost() {
        return Collections.unmodifiableSet(this.cost);
    }

    public Collection<ShopItem> getItems() {
        return this.items;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return "PlayerPurchaseItemEvent{" +
                "player=" + player +
                ", arena=" + arena +
                ", cost=" + cost +
                ", items=" + items +
                ", cancelled=" + cancelled +
                '}';
    }

}
