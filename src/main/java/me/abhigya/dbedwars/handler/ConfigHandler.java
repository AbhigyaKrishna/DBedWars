package me.abhigya.dbedwars.handler;

import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.configuration.PluginFiles;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableArena;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableCustomItems;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableItemSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ConfigHandler {

    private final DBedwars plugin;

    private final Set<ConfigurableItemSpawner> dropTypes;
    private final Set<ConfigurableArena> arenas;
    private ConfigurableCustomItems customItems;

    public ConfigHandler(DBedwars plugin) {
        this.plugin = plugin;
        this.dropTypes = new HashSet<>();
        this.arenas = new HashSet<>();
    }

    public void loadConfigurations() {
        this.loadArena();
        this.loadItemSpawners();
        this.plugin.getGameManager().load();
        this.customItems = new ConfigurableCustomItems();
        this.customItems.load(YamlConfiguration.loadConfiguration(PluginFiles.CUSTOM_ITEMS.getFile()));
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

    public Set<ConfigurableItemSpawner> getDropTypes() {
        return dropTypes;
    }

    public Set<ConfigurableArena> getArenas() {
        return arenas;
    }

    public ConfigurableCustomItems getCustomItems() {
        return customItems;
    }
}
