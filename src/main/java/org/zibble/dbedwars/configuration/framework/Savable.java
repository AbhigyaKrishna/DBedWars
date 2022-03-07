package org.zibble.dbedwars.configuration.framework;

import org.bukkit.configuration.ConfigurationSection;

public interface Savable {

    int save(ConfigurationSection section);

    default int saveEntries(ConfigurationSection section) {
        return ConfigSaver.save(this, section);
    }

    default int saveDefaults(ConfigurationSection section) {
        return ConfigSaver.saveDefaults(this, section);
    }

}
