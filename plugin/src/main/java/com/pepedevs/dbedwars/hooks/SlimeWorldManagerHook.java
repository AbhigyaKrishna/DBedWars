package com.pepedevs.dbedwars.hooks;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.pepedevs.corelib.utils.console.ConsoleUtils;
import com.pepedevs.corelib.utils.scheduler.SchedulerUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.handler.WorldAdaptor;
import com.pepedevs.dbedwars.configuration.PluginFiles;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SlimeWorldManagerHook implements WorldAdaptor {

    private final DBedwars plugin;
    private final SlimePlugin slime;
    private final File file;

    private FileConfiguration config;
    private SlimeLoader loader;

    public SlimeWorldManagerHook(DBedwars plugin) {
        this.plugin = plugin;
        this.slime =
                (SlimePlugin)
                        this.plugin.getServer().getPluginManager().getPlugin("SlimeWorldManager");
        this.file = PluginFiles.SLIME_WORLD_MANAGER_HOOK.getFile();
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void setup() {
        SchedulerUtils.runTaskAsynchronously(
                () -> {
                    if (this.isEnabled() && this.config.getBoolean("configure-slime-sources")) {
                        if (!this.config.getBoolean("disable-configure-message")) {
                            ConsoleUtils.sendPluginMessage(
                                    ChatColor.YELLOW
                                            + "We are configuring 'SlimeWorldManager/sources.yml'"
                                            + " for our convenience!",
                                    this.plugin);
                            ConsoleUtils.sendPluginMessage(
                                    ChatColor.YELLOW
                                            + "If you have any inconvenience with this feature,"
                                            + " please stop the server and disable it in"
                                            + " 'DBedWars/hooks/SlimeWorldManager-Hook.yml' and"
                                            + " revert your setting from"
                                            + " 'SlimeWorldManager/sources.yml.old'. But we highly"
                                            + " suggest you to leave this feature on! Or you may"
                                            + " not be able to use other DataSources for storage!",
                                    this.plugin);
                            ConsoleUtils.sendPluginMessage(
                                    ChatColor.YELLOW
                                            + "You could disable this message by setting"
                                            + " 'disable-configure-message' to 'true' in"
                                            + " 'DBedWars/hooks/SlimeWorldManager-Hook.yml'",
                                    this.plugin);
                        }
                        //                        this.configureSlimeConfig();
                    }
                    String[] availableLoaders = new String[] {"file", "mysql", "mongodb"};
                    String l = config.getString("slime-loader");
                    l =
                            l != null
                                    ? (Arrays.stream(availableLoaders)
                                                    .anyMatch(
                                                            x ->
                                                                    x.equalsIgnoreCase(
                                                                            config.getString(
                                                                                    "slime-loader")))
                                            ? l.toLowerCase(Locale.ROOT)
                                            : "file")
                                    : "file";
                    this.loader = slime.getLoader(l);
                },
                this.plugin);
    }

    public boolean isEnabled() {
        return this.file.exists() && this.config.getBoolean("enabled");
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    @Override
    public World createWorld(String worldName, World.Environment environment) {
        SlimePropertyMap spm = new SlimePropertyMap();
        spm.setString(SlimeProperties.WORLD_TYPE, "flat");
        spm.setInt(SlimeProperties.SPAWN_X, 0);
        spm.setInt(SlimeProperties.SPAWN_Y, 0);
        spm.setInt(SlimeProperties.SPAWN_Z, 0);
        spm.setBoolean(SlimeProperties.ALLOW_ANIMALS, this.getAllowAnimals(worldName));
        spm.setBoolean(SlimeProperties.ALLOW_MONSTERS, this.getAllowMonsters(worldName));
        spm.setString(
                SlimeProperties.DIFFICULTY,
                this.getDifficulty(worldName).name().toLowerCase(Locale.ROOT));
        spm.setString(
                SlimeProperties.ENVIRONMENT,
                this.getEnvironment(worldName).name().toLowerCase(Locale.ROOT));
        spm.setBoolean(SlimeProperties.PVP, true);

        try {
            SlimeWorld sw = slime.createEmptyWorld(loader, worldName, false, spm);
            this.slime.generateWorld(sw);
        } catch (WorldAlreadyExistsException | IOException e) {
            e.printStackTrace();
        }

        return Bukkit.getWorld(worldName);
    }

    @Override
    public World loadWorldFromFolder(String worldName) {
        if (!this.saveWorld(worldName, worldName)) {
            return null;
        }

        SlimePropertyMap spm = new SlimePropertyMap();
        spm.setBoolean(SlimeProperties.ALLOW_ANIMALS, this.getAllowMonsters(worldName));
        spm.setBoolean(SlimeProperties.ALLOW_MONSTERS, this.getAllowMonsters(worldName));
        spm.setString(
                SlimeProperties.DIFFICULTY,
                this.getDifficulty(worldName).name().toLowerCase(Locale.ROOT));
        spm.setBoolean(SlimeProperties.PVP, true);

        try {
            SlimeWorld sw = this.slime.loadWorld(this.loader, worldName, true, spm);
            this.slime.generateWorld(sw);
        } catch (UnknownWorldException
                | CorruptedWorldException
                | IOException
                | NewerFormatException
                | WorldInUseException e) {
            e.printStackTrace();
        }

        return Bukkit.getWorld(worldName);
    }

    @Override
    public World loadWorldFromSave(String fileName) {
        SlimePropertyMap spm = new SlimePropertyMap();
        spm.setString(SlimeProperties.WORLD_TYPE, "flat");
        spm.setBoolean(SlimeProperties.ALLOW_ANIMALS, this.getAllowAnimals(fileName));
        spm.setBoolean(SlimeProperties.ALLOW_MONSTERS, this.getAllowMonsters(fileName));
        spm.setString(
                SlimeProperties.DIFFICULTY,
                this.getDifficulty(fileName).name().toLowerCase(Locale.ROOT));
        spm.setBoolean(SlimeProperties.PVP, true);

        try {
            SlimeWorld sw = this.slime.loadWorld(this.loader, fileName, true, spm);
            this.slime.generateWorld(sw);
        } catch (UnknownWorldException
                | CorruptedWorldException
                | IOException
                | NewerFormatException
                | WorldInUseException e) {
            e.printStackTrace();
        }

        return Bukkit.getWorld(fileName);
    }

    @Override
    public boolean saveWorld(String worldName, String name) {
        try {
            this.slime.importWorld(new File(worldName), name, this.loader);
            return true;
        } catch (WorldAlreadyExistsException
                | WorldTooBigException
                | WorldLoadedException
                | InvalidWorldException
                | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean saveExist(String name) {
        try {
            return this.loader.worldExists(name);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void unloadWorld(String worldName, boolean save) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            List<Player> players = world.getPlayers();
            Location spawn = Bukkit.getWorld(this.plugin.getMainWorld()).getSpawnLocation();
            players.forEach(p -> p.teleport(spawn));
            Bukkit.unloadWorld(world, save);
        }
    }

    @Override
    public void deleteWorld(String worldName) {
        if (Bukkit.getWorld(worldName) != null) this.unloadWorld(worldName, false);

        try {
            this.loader.deleteWorld(worldName);
        } catch (UnknownWorldException | IOException e) {
            e.printStackTrace();
        }
    }

    private boolean getAllowAnimals(String worldName) {
        if (this.config.isConfigurationSection(worldName)) {
            return this.config.getBoolean(worldName + ".allow-animals");
        } else {
            return this.config.getBoolean("default.allow-animals", false);
        }
    }

    private boolean getAllowMonsters(String worldName) {
        if (this.config.isConfigurationSection(worldName)) {
            return this.config.getBoolean(worldName + ".allow-monsters");
        } else {
            return this.config.getBoolean("default.monsters", false);
        }
    }

    private Difficulty getDifficulty(String worldName) {
        String s =
                this.config.getString(
                        worldName + ".difficulty",
                        this.config.getString("default.difficulty", "EASY"));
        try {
            return Difficulty.valueOf(s);
        } catch (IllegalArgumentException e) {
            return Difficulty.EASY;
        }
    }

    private World.Environment getEnvironment(String worldName) {
        String s =
                this.config.getString(
                        worldName + ".environment",
                        this.config.getString("default.environment", "NORMAL"));
        try {
            return World.Environment.valueOf(s);
        } catch (IllegalArgumentException e) {
            return World.Environment.NORMAL;
        }
    }

    //    private void configureSlimeConfig() {
    //        String sourcesPath =
    //                this.config.getString("config", "plugins/SlimeWorldManager/sources.yml");
    //        File sources = new File(sourcesPath);
    //        if (!sources.exists()) {
    //            sources = new File("plugins/SlimeWorldManager/sources.yml");
    //        }
    //
    //        if (!sources.exists()) {
    //            throw new InvalidPathException(
    //                    sourcesPath,
    //                    "Please configure the path for \"SlimeWorldManager/sources.yml\" in \""
    //                            + this.file.getPath()
    //                            + "\"");
    //        }
    //
    //        boolean reloadSlime = false;
    //
    //        try {
    //            File oldFile = new File(sources.getAbsolutePath() + ".old");
    //            if (!file.exists()) {
    //                FileUtils.copyFile(sources, oldFile);
    //            }
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //
    //        if (!PluginFiles.ARENA_DATA_ARENACACHE.getFile().isDirectory())
    //            PluginFiles.ARENA_DATA_ARENACACHE.getFile().mkdirs();
    //
    //        FileConfiguration configuration = YamlConfiguration.loadConfiguration(sources);
    //
    //        // File DataSource
    //        String filePath =
    //                this.config.getString(
    //                        "sources.file.path", "plugins/DBedwars/arena/data/arenacache");
    //        String oldPath = configuration.getString("file.path");
    //
    //        if (!filePath.equals(oldPath)) {
    //            reloadSlime = true;
    //            File slimeFolder = new File(oldPath);
    //            if (slimeFolder.isDirectory()) {
    //                File[] slimeWorlds = slimeFolder.listFiles();
    //                for (File worldFile : slimeWorlds) {
    //                    if (!worldFile.getName().endsWith(".slime")) continue;
    //                    try {
    //                        FileUtils.copyFile(
    //                                worldFile,
    //                                new File(
    //                                        PluginFiles.ARENA_DATA_ARENACACHE.getFile(),
    //                                        worldFile.getName()));
    //                    } catch (IOException e) {
    //                        e.printStackTrace();
    //                    }
    //                }
    //            }
    //            configuration.set("file.path", filePath);
    //        }
    //
    //        // MYSQL DataSource
    //        ConfigurationSection mysqlSection =
    // this.config.getConfigurationSection("sources.mysql");
    //        ConfigurationSection sourceMysql = configuration.getConfigurationSection("mysql");
    //        if (mysqlSection == null) {
    //            mysqlSection = this.config.createSection("sources.mysql");
    //            for (String key : sourceMysql.getKeys(false)) {
    //                mysqlSection.set(key, sourceMysql.get(key));
    //            }
    //        }
    //
    //        boolean slimeConfigMysql = this.isDefaultMysql(sourceMysql);
    //        boolean hookConfigMysql = this.isDefaultMysql(mysqlSection);
    //
    //        if (!(slimeConfigMysql && hookConfigMysql)) {
    //            reloadSlime = true;
    //            if (!slimeConfigMysql) {
    //                for (String key : sourceMysql.getKeys(false)) {
    //                    mysqlSection.set(key, sourceMysql.get(key));
    //                }
    //            } else if (!hookConfigMysql) {
    //                for (String key : mysqlSection.getKeys(false)) {
    //                    sourceMysql.set(key, mysqlSection.get(key));
    //                }
    //            }
    //        } else {
    //            if (this.config.getString("slime-loader").equalsIgnoreCase("mysql")) {
    //                throw new ConfigurationException("Mysql database not configured!");
    //            }
    //        }
    //
    //        // MYSQL DataSource
    //        ConfigurationSection mongoSection =
    // this.config.getConfigurationSection("sources.mongodb");
    //        ConfigurationSection sourceMongo = configuration.getConfigurationSection("mongodb");
    //        if (mongoSection == null) {
    //            mongoSection = this.config.createSection("sources.mongodb");
    //            for (String key : sourceMongo.getKeys(false)) {
    //                mongoSection.set(key, sourceMongo.get(key));
    //            }
    //        }
    //
    //        boolean slimeConfigMongo = this.isDefaultMongoDB(sourceMongo);
    //        boolean hookConfigMongo = this.isDefaultMongoDB(mongoSection);
    //
    //        if (!(slimeConfigMongo && hookConfigMongo)) {
    //            reloadSlime = true;
    //            if (!slimeConfigMongo) {
    //                for (String key : sourceMongo.getKeys(false)) {
    //                    mongoSection.set(key, sourceMongo.get(key));
    //                }
    //            } else if (!hookConfigMongo) {
    //                for (String key : mongoSection.getKeys(false)) {
    //                    sourceMongo.set(key, mongoSection.get(key));
    //                }
    //            }
    //        } else {
    //            if (this.config.getString("slime-loader").equalsIgnoreCase("mongodb")) {
    //                throw new ConfigurationException("MongoDB database not configured!");
    //            }
    //        }
    //
    //        try {
    //            this.config.save(this.file);
    //            configuration.save(sources);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //
    //        if (reloadSlime) {
    //            try {
    //                ConfigManager.initialize();
    //            } catch (IOException | ObjectMappingException e) {
    //                e.printStackTrace();
    //            }
    //            LoaderUtils.registerLoaders();
    //        }
    //        this.reloadConfig();
    //    }

    private boolean isDefaultMysql(ConfigurationSection section) {
        try {
            return !section.getBoolean("enabled")
                    && Objects.equals(section.getString("host"), "127.0.0.1")
                    && section.getInt("port") == 3306
                    && Objects.equals(section.getString("username"), "slimeworldmanager")
                    && Objects.equals(section.getString("password"), "")
                    && Objects.equals(section.getString("database"), "slimeworldmanager");
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isDefaultMongoDB(ConfigurationSection section) {
        try {
            return !section.getBoolean("enabled")
                    && Objects.equals(section.getString("host"), "127.0.0.1")
                    && section.getInt("port") == 27017
                    && Objects.equals(section.getString("auth"), "admin")
                    && Objects.equals(section.getString("username"), "slimeworldmanager")
                    && Objects.equals(section.getString("password"), "")
                    && Objects.equals(section.getString("database"), "slimeworldmanager")
                    && Objects.equals(section.getString("collection"), "worlds");
        } catch (Exception ex) {
            return false;
        }
    }
}
