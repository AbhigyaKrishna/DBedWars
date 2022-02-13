package com.pepedevs.dbedwars.task.implementations;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.future.ActionFuture;
import com.pepedevs.dbedwars.api.hooks.world.WorldAdaptor;
import com.pepedevs.dbedwars.api.util.SchedulerUtils;
import com.pepedevs.dbedwars.api.version.Version;
import com.pepedevs.dbedwars.configuration.PluginFiles;
import com.pepedevs.dbedwars.utils.PluginFileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class WorldAdaptorImpl implements WorldAdaptor {

    private final DBedwars plugin;

    public WorldAdaptorImpl(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public ActionFuture<World> createWorld(String worldName, World.Environment environment) {
        if (Bukkit.getWorld(worldName) != null) return ActionFuture.completedFuture(Bukkit.getWorld(worldName));

        return ActionFuture.supplyAsync(new Supplier<World>() {
            @Override
            public World get() {
                CompletableFuture<World> future = new CompletableFuture<>();
                SchedulerUtils.runTask(new Runnable() {
                    @Override
                    public void run() {
                        WorldCreator wc = new WorldCreator(worldName);
                        wc.environment(environment);
                        wc.type(WorldType.FLAT);
                        if (plugin.getServerVersion().isNewerEquals(Version.v1_13_R1)) {
                            wc.generator(WorldAdaptorImpl.this.plugin.getDescription().getName());
                        } else {
                            wc.generatorSettings("2;0;1");
                        }
                        future.complete(wc.createWorld());
                    }
                });
                return future.join();
            }
        });
    }

    @Override
    public ActionFuture<World> loadWorldFromFolder(String worldName) {
        if (Bukkit.getWorld(worldName) != null) return ActionFuture.completedFuture(Bukkit.getWorld(worldName));

        return ActionFuture.supplyAsync(new Supplier<World>() {
            @Override
            public World get() {
                CompletableFuture<World> future = new CompletableFuture<>();
                SchedulerUtils.runTask(new Runnable() {
                    @Override
                    public void run() {
                        WorldCreator wc = new WorldCreator(worldName);
                        future.complete(wc.createWorld());
                    }
                });
                return future.join();
            }
        });
    }

    @Override
    public ActionFuture<World> loadWorldFromSave(String fileName) {
        World world = Bukkit.getWorld(fileName);
        ActionFuture<World> future = ActionFuture.completedFuture(world);
        if (world == null) {
            future = this.createWorld(fileName, World.Environment.NORMAL);
        }

        return future.thenApply(new Function<World, World>() {
            @Override
            public World apply(World world) {
                PluginFileUtils.copyWorldRegion(fileName, fileName);

                WorldAdaptorImpl.this.plugin.getNMSAdaptor().clearRegionFileCache(world);
                WorldAdaptorImpl.this.plugin.getNMSAdaptor().clearChunkCache(world);
                World finalWorld = world;
                WorldAdaptorImpl.this.plugin.getThreadHandler().runTaskLater(new Runnable() {
                    @Override
                    public void run() {
                        for (Player player : finalWorld.getPlayers()) {
                            plugin.getNMSAdaptor().refreshPlayerChunk(player);
                        }
                    }
                }, 20 * 50);
                return world;
            }
        });
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
