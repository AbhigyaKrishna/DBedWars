package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigurableScoreboard implements Loadable {

    private final String key;

    private List<String> title;

    private List<List<String>> content;

    @Defaults.Integer(5)
    @ConfigPath("update-tick")
    private int updateTick;

    public ConfigurableScoreboard(String key) {
        this.key = key;
    }

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
        if (section.isList("title")) {
            this.title = section.getStringList("title");
        } else {
            this.title = Collections.singletonList(section.getString("title"));
        }
        this.content = new ArrayList<>();
        if (section.isList("content")) {
            for (String s : section.getStringList("content")) {
                this.content.add(Collections.singletonList(s));
            }
        } else if (section.isConfigurationSection("content")) {
            ConfigurationSection content = section.getConfigurationSection("content");
            for (String s : content.getKeys(false)) {
                this.content.add(content.getStringList(s));
            }
        }
    }

    @Override
    public boolean isValid() {
        return !this.title.isEmpty();
    }

    public String getKey() {
        return key;
    }

    public List<String> getTitle() {
        return this.title;
    }

    public List<List<String>> getContent() {
        return this.content;
    }

    public int getUpdateTick() {
        return this.updateTick;
    }

    @Override
    public String toString() {
        return "ConfigurableScoreboard{" +
                "key='" + key + '\'' +
                ", title=" + title +
                ", content=" + content +
                ", updateTick=" + updateTick +
                '}';
    }

}
