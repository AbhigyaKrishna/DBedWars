package me.abhigya.dbedwars.api.events;

import me.Abhigya.core.events.CustomEvent;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.api.game.Team;
import org.bukkit.event.HandlerList;

public class PlayerBaseExitEvent extends CustomEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList( );

    private ArenaPlayer player;
    private Arena arena;
    private Team team;

    public PlayerBaseExitEvent( ArenaPlayer player, Arena arena, Team team ) {
        this.player = player;
        this.arena = arena;
        this.team = team;
    }

    public static HandlerList getHandlerList( ) {
        return HANDLER_LIST;
    }

    public ArenaPlayer getPlayer( ) {
        return player;
    }

    public Arena getArena( ) {
        return arena;
    }

    public Team getTeam( ) {
        return team;
    }

    @Override
    public HandlerList getHandlers( ) {
        return HANDLER_LIST;
    }

}
