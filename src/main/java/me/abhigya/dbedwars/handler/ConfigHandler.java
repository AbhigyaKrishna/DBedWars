package me.abhigya.dbedwars.handler;

import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.configuration.MainConfiguration;
import me.abhigya.dbedwars.configuration.PluginFiles;
import me.abhigya.dbedwars.configuration.configurable.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigHandler {

  private final DBedwars plugin;
  private final Set<ConfigurableItemSpawner> dropTypes;
  private final Set<ConfigurableArena> arenas;
  private final Set<ConfigurableTrap> traps;
  private final Set<ConfigurableScoreboard> scoreboards;
  private MainConfiguration mainConfiguration;
  private ConfigurableDatabase database;
  private ConfigurableShop shop;
  private ConfigurableUpgrade upgrade;
  private ConfigurableCustomItems customItems;
  private ConfigurableParticleImages particleImages;

  public ConfigHandler(DBedwars plugin) {
    this.plugin = plugin;
    this.dropTypes = new HashSet<>();
    this.arenas = new HashSet<>();
    this.traps = new HashSet<>();
    this.scoreboards = new HashSet<>();
  }

  public void loadConfigurations() {
    this.mainConfiguration = new MainConfiguration(this.plugin);
    this.mainConfiguration.load(YamlConfiguration.loadConfiguration(PluginFiles.CONFIG.getFile()));
    this.mainConfiguration.isValid();

    this.loadArena();
    this.loadItemSpawners();
    this.loadTraps();
    this.loadScoreBoards();
    this.database = new ConfigurableDatabase();
    this.database.load(YamlConfiguration.loadConfiguration(PluginFiles.DATABASE.getFile()));
    this.shop = new ConfigurableShop();
    this.shop.load(YamlConfiguration.loadConfiguration(PluginFiles.SHOP.getFile()));
    this.upgrade = new ConfigurableUpgrade();
    this.upgrade.load(YamlConfiguration.loadConfiguration(PluginFiles.UPGRADES.getFile()));
    this.plugin.getGameManager().load();
    this.customItems = new ConfigurableCustomItems();
    this.customItems.load(YamlConfiguration.loadConfiguration(PluginFiles.CUSTOM_ITEMS.getFile()));
    this.particleImages = new ConfigurableParticleImages();
    this.particleImages.load(
        YamlConfiguration.loadConfiguration(PluginFiles.PARTICLE_IMAGES.getFile()));
  }

  private void loadArena() {
    File folder = PluginFiles.ARENA_DATA_SETTINGS.getFile();
    for (File file : folder.listFiles()) {
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      ConfigurableArena cfg = new ConfigurableArena();
      cfg.load(config);
      this.arenas.add(cfg);
    }
  }

  private void loadItemSpawners() {
    File file = PluginFiles.ITEM_SPAWNERS.getFile();
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    for (String key : config.getKeys(false)) {
      ConfigurableItemSpawner spawner = new ConfigurableItemSpawner(this.plugin, key);
      spawner.load(config.getConfigurationSection(key));
      this.dropTypes.add(spawner);
    }
  }

  private void loadTraps() {
    File file = PluginFiles.TRAPS.getFile();
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    for (String key : config.getKeys(false)) {
      ConfigurableTrap trap = new ConfigurableTrap(key);
      trap.load(config.getConfigurationSection(key));
      this.traps.add(trap);
    }
  }

  private void loadScoreBoards() {
    File file = PluginFiles.SCOREBOARD.getFile();
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    for (String key : config.getKeys(false)) {
      ConfigurableScoreboard scoreboard = new ConfigurableScoreboard(key);
      scoreboard.load(config.getConfigurationSection(key));
      this.scoreboards.add(scoreboard);
    }
  }

  public MainConfiguration getMainConfiguration() {
    return this.mainConfiguration;
  }

  public Set<ConfigurableItemSpawner> getDropTypes() {
    return this.dropTypes;
  }

  public Set<ConfigurableArena> getArenas() {
    return this.arenas;
  }

  public Set<ConfigurableTrap> getTraps() {
    return this.traps;
  }

  public Set<ConfigurableScoreboard> getScoreboards() {
    return this.scoreboards;
  }

  public ConfigurableDatabase getDatabase() {
    return this.database;
  }

  public ConfigurableShop getShop() {
    return this.shop;
  }

  public ConfigurableUpgrade getUpgrade() {
    return this.upgrade;
  }

  public ConfigurableCustomItems getCustomItems() {
    return this.customItems;
  }

  public ConfigurableParticleImages getParticleImages() {
    return this.particleImages;
  }
}
