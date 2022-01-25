package com.pepedevs.dbedwars.api.events;

import com.pepedevs.radium.events.CustomEventCancellable;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.view.ShopView;
import org.bukkit.event.HandlerList;

public class PlayerOpenShopEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private ArenaPlayer player;
    private Arena arena;
    private ShopView shopView;

    public PlayerOpenShopEvent(ArenaPlayer player, Arena arena, ShopView shopView) {
        this.player = player;
        this.arena = arena;
        this.shopView = shopView;
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

    public ShopView getShopView() {
        return this.shopView;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return "PlayerOpenShopEvent{" +
                "player=" + player +
                ", arena=" + arena +
                ", shopView=" + shopView +
                ", cancelled=" + cancelled +
                '}';
    }
}
