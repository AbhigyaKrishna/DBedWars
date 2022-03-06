package org.zibble.dbedwars.configuration.framework;

import org.bukkit.configuration.ConfigurationSection;

public interface Savable {

    void save();

    default void saveEntries(ConfigurationSection section) {
        Saver.save(this, section);
    }

}
