package org.zibble.dbedwars.api.plugin;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

/**
 * Represents a possible plugin on which a {@link Plugin} may depend.
 *
 * <p>This class implements {@link Function} for receiving the plugin on which a {@link Plugin}
 * depends, and producing a resultant {@link Boolean}, the function might receive <strong>{@code
 * null}</strong> that means the plugin has never been loaded by Bukkit plugin manager, in that
 * case, if the developer return <strong>{@code null}</strong> from this function, the plugin will
 * be disabled automatically.
 *
 * <p>Also the developer can send messages to the console when checking the received plugin.
 */
public abstract class PluginDependence implements Function<Plugin, Boolean> {

    /**
     * The name of the depending plugin
     */
    protected final String name;

    protected boolean enabled;

    /**
     * Construct the plugin dependence. Note the plugin {@code name} is case-sensitive.
     *
     * <p>
     *
     * @param name Name of the depending plugin.
     */
    public PluginDependence(final String name) {
        Validate.notNull(name, "the name cannot be null!");
        this.name = name;
        this.enabled = Bukkit.getPluginManager().isPluginEnabled(name);
    }

    public void disable() {

    }

    /**
     * Gets the name of the depending plugin.
     *
     * <p>
     *
     * @return Name of the depending plugin.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns if the plugin found enable and hooked.
     *
     * <p>
     *
     * @return {@code true} if the plugin is enabled, false otherwise
     */
    public boolean isEnabled() {
        return this.enabled;
    }

}
