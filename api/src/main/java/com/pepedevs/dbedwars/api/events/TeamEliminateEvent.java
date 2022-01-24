package com.pepedevs.dbedwars.api.events;

import com.pepedevs.radium.events.CustomEvent;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;
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

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
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
}
