package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;

import java.util.List;

public class ConfigurableScoreboard implements Loadable {

    private final String key;

    @ConfigPath("title")
    private String title;

    @ConfigPath("content")
    private List<String> content;

    @ConfigPath("update-tick")
    private int updateTick;

    public ConfigurableScoreboard(String key) {
        this.key = key;
        this.updateTick = 5;
    }

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return this.title != null;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getContent() {
        return this.content;
    }

    public int getUpdateTick() {
        return this.updateTick;
    }

}
