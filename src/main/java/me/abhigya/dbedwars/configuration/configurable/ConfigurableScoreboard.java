package me.abhigya.dbedwars.configuration.configurable;

import me.Abhigya.core.util.loadable.Loadable;
import me.Abhigya.core.util.loadable.LoadableEntry;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class ConfigurableScoreboard implements Loadable {

  private final String key;

  @LoadableEntry(key = "title")
  private String title;

  @LoadableEntry(key = "content")
  private List<String> content;

  @LoadableEntry(key = "update-tick")
  private int updateTick;

  public ConfigurableScoreboard(String key) {
    this.key = key;
    this.updateTick = 5;
  }

  @Override
  public Loadable load(ConfigurationSection section) {
    return this.loadEntries(section);
  }

  @Override
  public boolean isValid() {
    return this.title != null;
  }

  @Override
  public boolean isInvalid() {
    return !this.isValid();
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
