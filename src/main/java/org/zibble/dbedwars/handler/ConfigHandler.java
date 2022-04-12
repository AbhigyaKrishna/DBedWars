package org.zibble.dbedwars.handler;

import com.google.gson.JsonElement;
import com.google.gson.stream.MalformedJsonException;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.configuration.MainConfiguration;
import org.zibble.dbedwars.configuration.PluginFiles;
import org.zibble.dbedwars.configuration.configurable.*;
import org.zibble.dbedwars.configuration.language.ConfigLang;
import org.zibble.dbedwars.io.ExternalLibrary;
import org.zibble.dbedwars.utils.Debugger;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigHandler {

    public static final String ERROR_WRITE_FILE = "Plugin resource directory `%s` cannot be created. Please check if the system has" +
            " required permissions to write files!";

    private final DBedwars plugin;
    private final Set<ConfigurableItemSpawner> dropTypes;
    private final Set<ConfigurableArenaCategory> categories;
    private final Set<ConfigurableArena> arenas;
    private final Set<ConfigurableTrap> traps;
    private final Set<ConfigurableScoreboard> scoreboards;
    private final Map<String, Json> jsonItem;
    private final Map<String, ConfigurableShop> shops;
    private final List<ConfigurableNpc> npc;
    private MainConfiguration mainConfiguration;
    private ConfigurableDatabase database;
    private ConfigurableCustomItems customItems;
    private ConfigurableHologram holograms;
    private ConfigurableLocation lobbyLocation;
    private ConfigurableEvents event;

    public ConfigHandler(DBedwars plugin) {
        this.plugin = plugin;
        this.dropTypes = new HashSet<>();
        this.categories = new HashSet<>();
        this.arenas = new HashSet<>();
        this.traps = new HashSet<>();
        this.scoreboards = new HashSet<>();
        this.jsonItem = new HashMap<>();
        this.shops = new HashMap<>();
        this.npc = new ArrayList<>();
    }

    public void initFiles() {
        boolean shouldDownloadAll = !PluginFiles.Folder.LIBRARIES_CACHE.isDirectory();

        for (File folder : PluginFiles.getDirectories()) {
            if (!folder.isDirectory())
                if (!folder.mkdirs()) {
                    this.plugin.getLogger().severe(String.format(ERROR_WRITE_FILE, folder.getName()));
                }
        }

        if (shouldDownloadAll) {
            ExternalLibrary.downloadAll();
        }

        for (File languageFile : PluginFiles.getLanguageFiles()) {
            this.plugin.saveResource("languages/" + languageFile.getName(), PluginFiles.Folder.LANGUAGES, false);
        }

        for (File file : PluginFiles.getFiles()) {
            String path = "";
            File parent = file.getParentFile();
            while (!parent.getName().equals(PluginFiles.Folder.PLUGIN_DATA_FOLDER.getName())) {
                path = parent.getName() + "/" + path;
                parent = parent.getParentFile();
            }
            this.plugin.saveResource(path + file.getName(), file.getParentFile(), false);
        }
    }

    public void initMainConfig() {
        this.mainConfiguration = new MainConfiguration();
        this.mainConfiguration.load(YamlConfiguration.loadConfiguration(PluginFiles.CONFIG));
        this.mainConfiguration.isValid();
        Debugger.setEnabled(this.mainConfiguration.isDebug());
        ActionFuture.setLogException(this.mainConfiguration.isDebug());
        Debugger.debug("Debugging is enabled!");
    }

    public void initLanguage() {
        ConfigLang.init(this.mainConfiguration.getLangSection());
        boolean loaded = false;
        for (File languageFile : PluginFiles.Folder.LANGUAGES.listFiles()) {
            if (languageFile.getName().replace(".yml", "").equals(this.mainConfiguration.getLangSection().getServerLanguage())) {
                loaded = true;
                ConfigLang.load(languageFile);
                break;
            }
        }

        if (!loaded)
            ConfigLang.load(PluginFiles.EN_US);
    }

    public void loadConfigurations() {
        this.loadArena();
        this.loadItemSpawners();
        this.loadTraps();
        this.loadScoreBoards();
        this.loadShops();
        this.database = new ConfigurableDatabase();
        this.database.load(YamlConfiguration.loadConfiguration(PluginFiles.DATABASE));
        this.customItems = new ConfigurableCustomItems();
        this.customItems.load(YamlConfiguration.loadConfiguration(PluginFiles.CUSTOM_ITEMS));
        this.holograms = new ConfigurableHologram();
        this.holograms.load(YamlConfiguration.loadConfiguration(PluginFiles.HOLOGRAM));
        this.event = new ConfigurableEvents();
        this.event.load(YamlConfiguration.loadConfiguration(PluginFiles.EVENT));

        if (PluginFiles.Data.LOBBY_SPAWN.exists()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(PluginFiles.Data.LOBBY_SPAWN);
            this.lobbyLocation = ConfigurableLocation.of(configuration);
        }
    }

    public void loadItems() {
        for (File file : PluginFiles.Folder.ITEMS.listFiles()) {
            if (file.getName().endsWith(".json")) {
                try {
                    Json json = Json.load(file);
                    for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                        this.jsonItem.put(entry.getKey(), Json.of(entry.getValue().getAsJsonObject()));
                    }
                } catch (MalformedJsonException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadArena() {
        for (File file : PluginFiles.Folder.CATEGORY.listFiles()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurableArenaCategory cfg = new ConfigurableArenaCategory();
            cfg.load(config);
            this.categories.add(cfg);
        }

        for (File file : PluginFiles.Folder.ARENA_DATA_SETTINGS.listFiles()) {
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

    private void loadShops() {
        File folder = PluginFiles.Folder.SHOPS;
        for (File file : folder.listFiles()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurableShop cfg = new ConfigurableShop();
            cfg.load(config);
            this.shops.put(file.getName().replace(".yml", "").replace(".yaml", ""), cfg);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(PluginFiles.NPC);
        for (String key : config.getKeys(false)) {
            if (!config.isConfigurationSection(key)) continue;
            ConfigurableNpc npc = new ConfigurableNpc();
            npc.load(config.getConfigurationSection(key));
            this.npc.add(npc);
        }
    }

    public void saveLobbyLocation(Location location) {
        ConfigurableLocation cfg = new ConfigurableLocation(location);
        if (!PluginFiles.Data.LOBBY_SPAWN.exists()) {
            try {
                PluginFiles.Data.LOBBY_SPAWN.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration section = YamlConfiguration.loadConfiguration(PluginFiles.Data.LOBBY_SPAWN);
        cfg.save(section);
        try {
            section.save(PluginFiles.Data.LOBBY_SPAWN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.lobbyLocation = cfg;
    }

    public MainConfiguration getMainConfiguration() {
        return this.mainConfiguration;
    }

    public Set<ConfigurableItemSpawner> getDropTypes() {
        return this.dropTypes;
    }

    public Set<ConfigurableArenaCategory> getCategories() {
        return categories;
    }

    public Set<ConfigurableArena> getArenas() {
        return this.arenas;
    }

    public Set<ConfigurableTrap> getTraps() {
        return this.traps;
    }

    public Map<String, Json> getJsonItem() {
        return jsonItem;
    }

    public Map<String, ConfigurableShop> getShops() {
        return shops;
    }

    public Set<ConfigurableScoreboard> getScoreboards() {
        return this.scoreboards;
    }

    public ConfigurableDatabase getDatabase() {
        return this.database;
    }

    public List<ConfigurableNpc> getNpc() {
        return npc;
    }

    public ConfigurableCustomItems getCustomItems() {
        return this.customItems;
    }

    public ConfigurableHologram getHolograms() {
        return holograms;
    }

    public ConfigurableEvents getEvent() {
        return event;
    }

    public ConfigurableLocation getLobbyLocation() {
        return lobbyLocation;
    }

}
