package com.pepedevs.dbedwars.api.events.player;

import com.pepedevs.dbedwars.api.events.CustomEvent;
import org.bukkit.entity.Player;

/** A player related {@link CustomEvent} */
public abstract class CustomPlayerEvent extends CustomEvent {

    /** The player involved in this event. */
    protected final Player player;

    /**
     * Constructing a player event.
     *
     * <p>
     *
     * @param player Player involved in this event.
     * @param async true indicates the event will fire asynchronously, false by default from default
     *     constructor
     */
    public CustomPlayerEvent(Player player, boolean async) {
        super(async);
        this.player = player;
    }

    /**
     * Constructing a synchronous player event.
     *
     * <p>
     *
     * @param player Player involved in this event.
     */
    public CustomPlayerEvent(final Player player) {
        this(player, false);
    }

    /**
     * Gets the player involved in this event.
     *
     * <p>
     *
     * @return Player who is involved in this event.
     */
    public Player getPlayer() {
        return player;
    }
}
