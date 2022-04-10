package org.zibble.dbedwars.hooks.slimeworldmanager;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.hooks.world.WorldAdaptor;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.plugin.PluginDependence;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SlimeWorldManagerHook extends PluginDependence implements WorldAdaptor {

    private final File file;
    private final World defaultWorld;
    private YamlConfiguration config;
    private SlimePlugin slimePlugin;
    private SlimeLoader loader;

    public SlimeWorldManagerHook(File hookFile, World defaultWorld) {
        super("SlimeWorldManager");
        this.file = hookFile;
        this.defaultWorld = defaultWorld;
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into SlimeWorldManager!"));
            this.slimePlugin = (SlimePlugin) plugin;
            this.config = YamlConfiguration.loadConfiguration(this.file);
            this.loader = this.slimePlugin.getLoader(this.config.getString("slime-loader"));
        }
        return true;
    }

    @Override
    public void disable() {
    }

    @Override
    public ActionFuture<World> createWorld(String worldName, World.Environment environment) {
        return ActionFuture.supplyAsync(() -> {
            SlimePropertyMap spm = new SlimePropertyMap();
            spm.setString(SlimeProperties.WORLD_TYPE, "flat");
            spm.setInt(SlimeProperties.SPAWN_X, 0);
            spm.setInt(SlimeProperties.SPAWN_Y, 0);
            spm.setInt(SlimeProperties.SPAWN_Z, 0);
            spm.setBoolean(SlimeProperties.ALLOW_ANIMALS, this.getAllowAnimals(worldName));
            spm.setBoolean(SlimeProperties.ALLOW_MONSTERS, this.getAllowMonsters(worldName));
            spm.setString(SlimeProperties.DIFFICULTY, this.getDifficulty(worldName).name().toLowerCase(Locale.ROOT));
            spm.setString(SlimeProperties.ENVIRONMENT, this.getEnvironment(worldName).name().toLowerCase(Locale.ROOT));
            spm.setBoolean(SlimeProperties.PVP, true);

            try {
                SlimeWorld sw = slimePlugin.createEmptyWorld(loader, worldName, false, spm);
                this.slimePlugin.generateWorld(sw);
            } catch (WorldAlreadyExistsException | IOException e) {
                e.printStackTrace();
            }

            return Bukkit.getWorld(worldName);
        });
    }

    @Override
    public ActionFuture<World> loadWorldFromFolder(String worldName, World.Environment environment) {
        return ActionFuture.supplyAsync(() -> {
            if (!this.saveWorld(worldName, worldName)) {
                return null;
            }

            SlimePropertyMap spm = new SlimePropertyMap();
            spm.setBoolean(SlimeProperties.ALLOW_ANIMALS, this.getAllowMonsters(worldName));
            spm.setBoolean(SlimeProperties.ALLOW_MONSTERS, this.getAllowMonsters(worldName));
            spm.setString(SlimeProperties.DIFFICULTY, this.getDifficulty(worldName).name().toLowerCase(Locale.ROOT));
            spm.setString(SlimeProperties.ENVIRONMENT, environment.name().toLowerCase(Locale.ROOT));
            spm.setBoolean(SlimeProperties.PVP, true);

            return this.loadWorld(worldName, spm);
        });
    }

    @Override
    public ActionFuture<World> loadWorldFromSave(String fileName, String worldName, World.Environment environment) {
        return ActionFuture.supplyAsync(() -> {
            SlimePropertyMap spm = new SlimePropertyMap();
            spm.setString(SlimeProperties.WORLD_TYPE, "flat");
            spm.setBoolean(SlimeProperties.ALLOW_ANIMALS, this.getAllowAnimals(fileName));
            spm.setBoolean(SlimeProperties.ALLOW_MONSTERS, this.getAllowMonsters(fileName));
            spm.setString(SlimeProperties.DIFFICULTY, this.getDifficulty(fileName).name().toLowerCase(Locale.ROOT));
            spm.setString(SlimeProperties.ENVIRONMENT, environment.name().toLowerCase(Locale.ROOT));
            spm.setBoolean(SlimeProperties.PVP, true);

            return this.loadWorld(fileName, spm);
        });
    }

    private World loadWorld(String fileName, SlimePropertyMap spm) {
        try {
            SlimeWorld sw = this.slimePlugin.loadWorld(this.loader, fileName, true, spm);
            this.slimePlugin.generateWorld(sw);
        } catch (UnknownWorldException | CorruptedWorldException | IOException | NewerFormatException | WorldInUseException e) {
            e.printStackTrace();
        }

        return Bukkit.getWorld(fileName);
    }

    @Override
    public boolean saveWorld(String worldName, String name) {
        try {
            this.slimePlugin.importWorld(new File(worldName), name, this.loader);
            return true;
        } catch (WorldAlreadyExistsException | WorldTooBigException | WorldLoadedException | InvalidWorldException | IOException e) {
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
            Location spawn = this.defaultWorld.getSpawnLocation();
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
        String s = this.config.getString(worldName + ".difficulty", this.config.getString("default.difficulty", "EASY"));
        try {
            return Difficulty.valueOf(s);
        } catch (IllegalArgumentException e) {
            return Difficulty.EASY;
        }
    }

    private World.Environment getEnvironment(String worldName) {
        String s = this.config.getString(worldName + ".environment", this.config.getString("default.environment", "NORMAL"));
        try {
            return World.Environment.valueOf(s);
        } catch (IllegalArgumentException e) {
            return World.Environment.NORMAL;
        }
    }

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
