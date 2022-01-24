package com.pepedevs.dbedwars.api.events;

import com.pepedevs.radium.events.CustomEventCancellable;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ArenaEndEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final Collection<ArenaPlayer> winners;

    public ArenaEndEvent(Arena arena, Collection<ArenaPlayer> winners) {
        this.arena = arena;
        this.winners = winners;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Arena getArena() {
        return this.arena;
    }

    public Collection<ArenaPlayer> getWinners() {
        return this.winners;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
