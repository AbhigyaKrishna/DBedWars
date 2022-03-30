package org.zibble.dbedwars.api.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.Team;

public class ArenaEndEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final Team winner;

    public ArenaEndEvent(Arena arena, Team winner) {
        this.arena = arena;
        this.winner = winner;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Arena getArena() {
        return this.arena;
    }

    public Team getWinner() {
        return this.winner;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
