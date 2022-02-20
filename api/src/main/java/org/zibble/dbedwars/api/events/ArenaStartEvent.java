package org.zibble.dbedwars.api.events;

import com.pepedevs.radium.events.CustomEventCancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.ArenaPlayer;

import java.util.Collections;
import java.util.Set;

public class ArenaStartEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Arena arena;
    private Set<ArenaPlayer> players;

    public ArenaStartEvent(Arena arena) {
        this.arena = arena;
        this.players = arena.getPlayers();
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Arena getArena() {
        return this.arena;
    }

    public Set<ArenaPlayer> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return "ArenaStartEvent{" +
                "arena=" + arena +
                ", players=" + players +
                ", cancelled=" + cancelled +
                '}';
    }
}
