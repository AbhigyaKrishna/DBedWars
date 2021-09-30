package me.abhigya.dbedwars.configuration.configurable;

import me.Abhigya.core.util.loadable.Loadable;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurableUpgrade implements Loadable {

    @Override
    public Loadable load(ConfigurationSection section) {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

}