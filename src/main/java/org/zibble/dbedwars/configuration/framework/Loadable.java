package org.zibble.dbedwars.configuration.framework;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.util.mixin.Validable;

public interface Loadable extends Validable {

    void load(ConfigurationSection section);

    default void loadEntries(ConfigurationSection section) {
        ConfigLoader.load(this, section);
    }

}