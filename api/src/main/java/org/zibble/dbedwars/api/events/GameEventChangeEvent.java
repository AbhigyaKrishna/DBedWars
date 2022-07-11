package org.zibble.dbedwars.api.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.GameEvent;

public class GameEventChangeEvent extends CustomEventCancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Arena arena;
    private final GameEvent event;
    private final GameEvent newEvent;

    public GameEventChangeEvent(Arena arena, GameEvent gameEvent, GameEvent newGameEvent) {
        this.arena = arena;
        this.event = gameEvent;
        this.newEvent = newGameEvent;
    }

    public Arena getArena() {
        return arena;
    }

    public GameEvent getEvent() {
        return event;
    }

    public GameEvent getNewEvent() {
        return newEvent;
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
