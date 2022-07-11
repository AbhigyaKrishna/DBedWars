package org.zibble.dbedwars.api.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.GameEvent;

public class PostGameEventChangeEvent extends CustomEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final GameEvent event;
    private final GameEvent oldEvent;

    public PostGameEventChangeEvent(Arena arena, GameEvent gameEvent, GameEvent oldGameEvent) {
        this.arena = arena;
        this.event = gameEvent;
        this.oldEvent = oldGameEvent;
    }

    public Arena getArena() {
        return arena;
    }

    public GameEvent getEvent() {
        return event;
    }

    public GameEvent getOldEvent() {
        return oldEvent;
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
