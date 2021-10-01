package me.abhigya.dbedwars.configuration.configurabletrap;

import me.Abhigya.core.util.loadable.Loadable;
import me.Abhigya.core.util.loadable.LoadableCollectionEntry;
import me.Abhigya.core.util.loadable.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConfigurableTrap implements Loadable {

    private final String key;

    @LoadableEntry(key = "id")
    private String id;

    @LoadableEntry(key = "trigger")
    private String trigger;

    @LoadableCollectionEntry(subsection = "actions")
    private List<ConfigurableTrapAction> trapActions;

    public ConfigurableTrap(String key) {
        trapActions = new ArrayList<>();
        this.key = key;
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return this.id != null && this.getTrigger() != null && !this.trapActions.isEmpty() && this.trapActions.stream().allMatch(ConfigurableTrapAction::isValid);
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

    public TrapEnum.TriggerType getTrigger() {
        return TrapEnum.TriggerType.matchTrigger(this.trigger);
    }

    public List<ConfigurableTrapAction> getTrapActions() {
        return trapActions;
    }

    public static class ConfigurableTrapAction implements Loadable {

        @LoadableEntry(key = "target")
        private String target;

        @LoadableEntry(key = "executable")
        private List<String> executables;

        protected ConfigurableTrapAction() {
            this.executables = new LinkedList<>();
        }

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
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
    }
}
