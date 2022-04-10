package org.zibble.dbedwars.api.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for handling {@link Plugin}. Also this is an implementation of {@link Listener}, so the
 * events in this class can be registered by using {@link #register()} or unregister by using {@link
 * #unregister()}.
 *
 * <p>Also {@link #isAllowMultipleInstances()} allows the developer to protect its handler from the
 * creation of more than one instance of it. Also {@link #isAllowMultipleInstances()} or the class
 * extending {@link PluginHandler} must be <strong>{@code final}</strong> to prevent overriding.
 */
public abstract class PluginHandler implements Listener {

    /**
     * Map for storing handler instances.
     */
    protected static final Map<Class<? extends PluginHandler>, PluginHandler> HANDLER_INSTANCES = new ConcurrentHashMap<>();
    /**
     * The handling plugin
     */
    protected final Plugin plugin;

    /**
     * Constructs the plugin handler.
     *
     * <p>
     */
    public PluginHandler(Plugin plugin) {
        if (!this.isAllowMultipleInstances()) {
            if (HANDLER_INSTANCES.containsKey(this.getClass()))
                throw new IllegalStateException("Cannot create more than one instance of this handler!");
        }

        this.plugin = plugin;
        HANDLER_INSTANCES.put(this.getClass(), this);
    }

    /**
     * This method provides fast access to the plugin handler that has provided the given class.
     *
     * <p>
     *
     * @param <T>   Class that extends PluginHandler
     * @param clazz Class desired
     * @return Instance of the plugin handle that provided the class
     */
    public static <T extends PluginHandler> PluginHandler getPluginHandler(Class<T> clazz) {
        return HANDLER_INSTANCES.get(clazz);
    }

    /**
     * Gets the plugin this handler handles.
     *
     * <p>
     *
     * @return The handling plugin.
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets whether this handler allows the creation of more than one instance of it.
     *
     * <p>This is useful to avoid users to create instances of this handler.
     *
     * <p>
     *
     * @return true to allow more than one instance
     */
    protected abstract boolean isAllowMultipleInstances();

    /**
     * Registers events in this class.
     */
    protected void register() {
        HandlerList.unregisterAll(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Unregisters events in this class.
     */
    protected void unregister() {
        HandlerList.unregisterAll(this);
    }

}
