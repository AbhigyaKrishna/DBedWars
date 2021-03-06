package me.abhigya.dbedwars.api.events.game;

import me.Abhigya.core.events.CustomEvent;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.Team;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TeamEliminateEvent extends CustomEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final Team team;

    public TeamEliminateEvent(Arena arena, Team team) {
        this.arena = arena;
        this.team = team;
    }

    public Arena getArena() {
        return arena;
    }

    public Team getTeam() {
        return team;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
