package org.zibble.dbedwars.api.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;

import java.util.Collection;

public class ArenaEndEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final Collection<? extends ArenaPlayer> winners;

    public ArenaEndEvent(Arena arena, Collection<? extends ArenaPlayer> winners) {
        this.arena = arena;
        this.winners = winners;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Arena getArena() {
        return this.arena;
    }

    public Collection<? extends ArenaPlayer> getWinners() {
        return this.winners;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return "ArenaEndEvent{" +
                "arena=" + arena +
                ", winners=" + winners +
                ", cancelled=" + cancelled +
                '}';
    }
}
