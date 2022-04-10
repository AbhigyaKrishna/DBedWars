package org.zibble.dbedwars.api.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

/**
 * Simple implementation of {@link Event} that can call itself.
 */
public abstract class CustomEvent extends Event {

    /**
     * The default constructor is defined for cleaner code. This constructor assumes the event is
     * synchronous.
     */
    public CustomEvent() {
        super();
    }

    /**
     * This constructor is used to explicitly declare an event as synchronous or asynchronous.
     *
     * <p>
     *
     * @param async true indicates the event will fire asynchronously, false by default from default
     *              constructor
     */
    public CustomEvent(boolean async) {
        super(async);
    }

    /**
     * Calls this event.
     *
     * <p>This is the same as:
     *
     * <pre><code>
     * Bukkit.getPluginManager().callEvent(this);
     * </code></pre>
     *
     * <p>
     *
     * @return This Object, for chaining.
     * @throws IllegalStateException thrown when an asynchronous event is fired from synchronous
     *                               code.
     *                               <p><i>Note: This is best-effort basis, and should not be used to test synchronized state.
     *                               This is an indicator for flawed flow logic.</i>
     */
    public CustomEvent call() throws IllegalStateException {
        Bukkit.getPluginManager().callEvent(this);
        return this;
    }

}
