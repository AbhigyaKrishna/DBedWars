package org.zibble.dbedwars.api.events;

import com.pepedevs.radium.events.CustomEvent;
import org.bukkit.event.HandlerList;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;

public class PlayerBaseExitEvent extends CustomEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private ArenaPlayer player;
    private Arena arena;
    private Team team;

    public PlayerBaseExitEvent(ArenaPlayer player, Arena arena, Team team) {
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

    @Override
    public String toString() {
        return "PlayerBaseExitEvent{" +
                "player=" + player +
                ", arena=" + arena +
                ", team=" + team +
                '}';
    }
}