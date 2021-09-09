package me.abhigya.dbedwars.configuration.configurabletrap;

import me.Abhigya.core.util.loadable.Loadable;
import me.Abhigya.core.util.loadable.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class ConfigurableTrap implements Loadable {

    private String key;

    @LoadableEntry(key = "id")
    private String id;

    @LoadableEntry(key = "actions")
    private List<ConfigurableTrapAction> trapActions;

    public ConfigurableTrap(String key) {
        this.key = key;
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return this.id != null;
    }

    @Override
    public boolean isInvalid() {
        return !this.isValid();
    }

    public String getKey() {
        return key;
    }

    public String getId() {
        return id;
    }

    public List<ConfigurableTrapAction> getTrapActions() {
        return trapActions;
    }

    public static class ConfigurableTrapAction implements Loadable {

        @LoadableEntry(key = "target")
        private String target;

        @LoadableEntry(key = "trigger")
        private String trigger;

        @LoadableEntry(key = "executable")
        private List<String> executables;

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return this.target != null && this.trigger != null && this.executables != null;
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
        }

        public String getTarget() {
            return target;
        }

        public String getTrigger() {
            return trigger;
        }

        public List<String> getExecutables() {
            return executables;
        }
    }
}
