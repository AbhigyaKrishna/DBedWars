package org.zibble.dbedwars.api.events;

import org.bukkit.event.Cancellable;

/** An event that implements {@link Cancellable}, allowing its cancellation. */
public abstract class CustomEventCancellable extends CustomEvent implements Cancellable {

    /** Whether this event is cancelled or not. */
    protected boolean cancelled;

    public CustomEventCancellable() {
    }

    public CustomEventCancellable(boolean async) {
        super(async);
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
