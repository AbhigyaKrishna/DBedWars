package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigurableEvents implements Loadable {

    @ConfigPath
    private List<String> order = new ArrayList<>();

    @ConfigPath
    private Map<String, Event> events;

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public List<String> getOrder() {
        return order;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public static class Event implements Loadable {

        @ConfigPath
        private String name;

        @ConfigPath
        private String duration;

        @ConfigPath
        private List<String> onStart;

        @ConfigPath
        private List<String> perTick;

        @ConfigPath
        private List<String> onEnd;

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public String getName() {
            return name;
        }

        public String getDuration() {
            return duration;
        }

        public List<String> getOnStart() {
            return onStart;
        }

        public List<String> getPerTick() {
            return perTick;
        }

        public List<String> getOnEnd() {
            return onEnd;
        }

    }

}
