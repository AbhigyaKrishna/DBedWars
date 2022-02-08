package com.pepedevs.dbedwars.task;

import com.pepedevs.radium.utils.version.Version;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.handler.WorldAdaptor;
import com.pepedevs.dbedwars.configuration.PluginFiles;
import com.pepedevs.dbedwars.utils.PluginFileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class DefaultWorldAdaptor implements WorldAdaptor {

    private final DBedwars plugin;

    public DefaultWorldAdaptor(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public World createWorld(String worldName, World.Environment environment) {
        if (Bukkit.getWorld(worldName) != null) return Bukkit.getWorld(worldName);

        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(environment);
        wc.type(WorldType.FLAT);
        if (plugin.getServerVersion().isNewerEquals(Version.v1_13_R1)) {
            wc.generator(this.plugin.getDescription().getName());
        } else {
            wc.generatorSettings("2;0;1");
        }
        return wc.createWorld();
    }

    @Override
    public World loadWorldFromFolder(String worldName) {
        if (Bukkit.getWorld(worldName) != null) return Bukkit.getWorld(worldName);

        WorldCreator wc = new WorldCreator(worldName);
        return wc.createWorld();
    }

    @Override
    public World loadWorldFromSave(String fileName) {
        World world = Bukkit.getWorld(fileName);
        if (world == null) {
            world = this.createWorld(fileName, World.Environment.NORMAL);
        }

        PluginFileUtils.copyWorldRegion(fileName, fileName);

        this.plugin.getNMSAdaptor().clearRegionFileCache(world);
        this.plugin.getNMSAdaptor().clearChunkCache(world);
        World finalWorld = world;
        this.plugin.getThreadHandler().runTaskLater(new Runnable() {
            @Override
            public void run() {
                for (Player player : finalWorld.getPlayers()) {
                    plugin.getNMSAdaptor().refreshPlayerChunk(player);
                }
            }
        }, 20 * 50);
        return world;
    }

    @Override
    public boolean saveWorld(String worldName, String fileName) {
        this.unloadWorld(worldName, true);

        return PluginFileUtils.saveWorldRegions(worldName, fileName);
    }

    @Override
    public boolean saveExist(String name) {
        File file = new File(PluginFiles.ARENA_DATA_ARENACACHE, name + ".zip");
        return file.exists();
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
        for (File file : PluginFiles.ARENA_DATA_ARENACACHE.listFiles()) {
            if (file.getName().equals(worldName + ".yml")) file.delete();
        }
    }
}
