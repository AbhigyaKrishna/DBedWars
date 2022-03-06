package org.zibble.dbedwars.configuration.framework;

import org.bukkit.configuration.ConfigurationSection;

public interface Loadable {

    void load(ConfigurationSection section);

    default void loadEntries(ConfigurationSection section) {
        Loader.load(this, section);
    }

}