package com.pepedevs.dbedwars.api.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/** A player related event that implements {@link Cancellable}, allowing its cancellation. */
public abstract class CustomPlayerEventCancellable extends CustomPlayerEvent
        implements Cancellable {

    /** Whether the event is cancelled or not */
    protected boolean cancelled;

    /**
     * Constructing a player event.
     *
     * <p>
     *
     * @param player Player involved in this event.
     * @param async true indicates the event will fire asynchronously, false by default from default
     *     constructor
     */
    public CustomPlayerEventCancellable(Player player, boolean async) {
        super(player, async);
    }

    /**
     * Constructing a synchronous player event.
     *
     * <p>
     *
     * @param player Player involved in this event.
     */
    public CustomPlayerEventCancellable(final Player player) {
        super(player);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
