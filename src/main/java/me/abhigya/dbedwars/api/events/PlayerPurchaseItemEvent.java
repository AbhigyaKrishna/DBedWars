package me.abhigya.dbedwars.api.events;

import me.Abhigya.core.events.CustomEventCancellable;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.util.BwItemStack;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class PlayerPurchaseItemEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList( );

    private ArenaPlayer player;
    private Arena arena;
    private Set< ItemStack > cost;
    private Collection< BwItemStack > items;

    public PlayerPurchaseItemEvent( ArenaPlayer player, Arena arena, Set< ItemStack > cost, Collection< BwItemStack > items ) {
        this.player = player;
        this.arena = arena;
        this.cost = cost;
        this.items = items;
    }

    public static HandlerList getHandlerList( ) {
        return HANDLER_LIST;
    }

    public ArenaPlayer getPlayer( ) {
        return this.player;
    }

    public Arena getArena( ) {
        return this.arena;
    }

    public Set< ItemStack > getCost( ) {
        return this.cost;
    }

    public Collection< BwItemStack > getItems( ) {
        return Collections.unmodifiableCollection( this.items );
    }

    @Override
    public HandlerList getHandlers( ) {
        return HANDLER_LIST;
    }

}
