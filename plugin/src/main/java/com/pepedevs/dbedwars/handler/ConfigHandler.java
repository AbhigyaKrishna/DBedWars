package com.pepedevs.dbedwars.handler;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.configuration.MainConfiguration;
import com.pepedevs.dbedwars.configuration.PluginFiles;
import com.pepedevs.dbedwars.configuration.configurable.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigHandler {

    public static final String ERROR_WRITE_FILE = "Plugin resource directory `%s` cannot be created. Please check if the system has" +
            " required permissions to write files!";

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

    public ConfigHandler(DBedwars plugin) {
        this.plugin = plugin;
        this.dropTypes = new HashSet<>();
        this.arenas = new HashSet<>();
        this.traps = new HashSet<>();
        this.scoreboards = new HashSet<>();
    }

    public void initFiles() {
        for (File folder : PluginFiles.getDirectories()) {
            if (!folder.isDirectory())
                if (!folder.mkdirs()) {
                    this.plugin.getLogger().severe(String.format(ERROR_WRITE_FILE, folder.getName()));
                }
        }

        for (File languageFile : PluginFiles.getLanguageFiles()) {
            this.plugin.saveResource("languages/" + languageFile.getName(), PluginFiles.LANGUAGES, false);
        }

        for (File file : PluginFiles.getFiles()) {
            String path = "";
            File parent = file.getParentFile();
            while (!parent.getName().equals(PluginFiles.PLUGIN_DATA_FOLDER.getName())) {
                path = parent.getName() + "/" + path;
                parent = parent.getParentFile();
            }
            this.plugin.saveResource(path + file.getName(), file.getParentFile(), false);
        }
    }

    public void initMainConfig() {
        this.mainConfiguration = new MainConfiguration(this.plugin);
        this.mainConfiguration.load(YamlConfiguration.loadConfiguration(PluginFiles.CONFIG));
        this.mainConfiguration.isValid();
    }

    public void initLanguage() {
        Lang.init(this.mainConfiguration.getLangSection());
        boolean loaded = false;
        for (File languageFile : PluginFiles.LANGUAGES.listFiles()) {
            if (languageFile.getName().replace(".yml", "").equals(this.mainConfiguration.getLangSection().getServerLanguage())) {
                loaded = true;
                Lang.load(languageFile);
                break;
            }
        }

        if (!loaded)
            Lang.load(PluginFiles.EN_US);
    }

    public void loadConfigurations() {
        this.loadArena();
        this.loadItemSpawners();
        this.loadTraps();
        this.loadScoreBoards();
        this.database = new ConfigurableDatabase();
        this.database.load(YamlConfiguration.loadConfiguration(PluginFiles.DATABASE));
        this.shop = new ConfigurableShop();
        this.shop.load(YamlConfiguration.loadConfiguration(PluginFiles.SHOP));
        this.upgrade = new ConfigurableUpgrade();
        this.upgrade.load(YamlConfiguration.loadConfiguration(PluginFiles.UPGRADES));
        this.plugin.getGameManager().load();
        this.customItems = new ConfigurableCustomItems();
        this.customItems.load(YamlConfiguration.loadConfiguration(PluginFiles.CUSTOM_ITEMS));
    }

    private void loadArena() {
        File folder = PluginFiles.ARENA_DATA_SETTINGS;
        for (File file : folder.listFiles()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurableArena cfg = new ConfigurableArena();
            cfg.load(config);
            this.arenas.add(cfg);
        }
    }

    private void loadItemSpawners() {
        File file = PluginFiles.ITEM_SPAWNERS;
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            ConfigurableItemSpawner spawner = new ConfigurableItemSpawner(this.plugin, key);
            spawner.load(config.getConfigurationSection(key));
            this.dropTypes.add(spawner);
        }
    }

    private void loadTraps() {
        File file = PluginFiles.TRAPS;
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : config.getKeys(false)) {
            ConfigurableTrap trap = new ConfigurableTrap(key);
            trap.load(config.getConfigurationSection(key));
            this.traps.add(trap);
        }
    }

    private void loadScoreBoards() {
        File file = PluginFiles.SCOREBOARD;
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

}
