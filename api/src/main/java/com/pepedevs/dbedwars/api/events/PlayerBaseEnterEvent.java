package com.pepedevs.dbedwars.api.events;

import com.pepedevs.radium.events.CustomEvent;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import org.bukkit.event.HandlerList;

public class PlayerBaseEnterEvent extends CustomEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private ArenaPlayer player;
    private Arena arena;
    private Team team;

    public PlayerBaseEnterEvent(ArenaPlayer player, Arena arena, Team team) {
        this.player = player;
        this.arena = arena;
        this.team = team;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public ArenaPlayer getPlayer() {
        return player;
    }

    public Arena getArena() {
        return arena;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
