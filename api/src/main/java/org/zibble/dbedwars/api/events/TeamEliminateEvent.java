package org.zibble.dbedwars.api.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.Team;

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

    @Override
    public String toString() {
        return "TeamEliminateEvent{" +
                "arena=" + arena +
                ", team=" + team +
                '}';
    }

}
