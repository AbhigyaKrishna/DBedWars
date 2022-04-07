package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConfigurableTrap implements Loadable {

    private final String key;

    @ConfigPath
    private String id;

    @ConfigPath
    private String trigger;

    @ConfigPath("actions")
    private List<ConfigurableTrapAction> trapActions;

    public ConfigurableTrap(String key) {
        this.key = key;
        this.trapActions = new ArrayList<>();
    }

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return this.id != null
                && this.getTrigger() != null
                && !this.trapActions.isEmpty()
                && this.trapActions.stream().allMatch(ConfigurableTrapAction::isValid);
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

    public String getTrigger() {
        return this.trigger;
    }

    public List<ConfigurableTrapAction> getTrapActions() {
        return trapActions;
    }

    public static class ConfigurableTrapAction implements Loadable {

        @ConfigPath
        private String target;

        @ConfigPath("executable")
        private List<String> executables;

        protected ConfigurableTrapAction() {
            this.executables = new LinkedList<>();
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return this.target != null && !this.executables.isEmpty();
        }

        @Override
        public boolean isInvalid() {
            return !this.isValid();
        }

        public String getTarget() {
            return target;
        }

        public List<String> getExecutables() {
            return executables;
        }

        @Override
        public String toString() {
            return "ConfigurableTrapAction{" +
                    "target='" + target + '\'' +
                    ", executables=" + executables +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ConfigurableTrap{" +
                "key='" + key + '\'' +
                ", id='" + id + '\'' +
                ", trigger='" + trigger + '\'' +
                ", trapActions=" + trapActions +
                '}';
    }
}
