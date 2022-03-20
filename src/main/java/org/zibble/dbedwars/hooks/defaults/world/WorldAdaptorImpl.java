package org.zibble.dbedwars.hooks.defaults.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.hooks.world.WorldAdaptor;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.SchedulerUtils;
import org.zibble.dbedwars.api.version.Version;
import org.zibble.dbedwars.configuration.PluginFiles;
import org.zibble.dbedwars.utils.PluginFileUtils;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class WorldAdaptorImpl implements WorldAdaptor {

    private final DBedwars plugin;

    public WorldAdaptorImpl(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public ActionFuture<World> createWorld(String worldName, World.Environment environment) {
        return ActionFuture.supplyAsync(() -> {
            CompletableFuture<World> future = new CompletableFuture<>();
            SchedulerUtils.runTask(() -> {
                WorldCreator wc = new WorldCreator(worldName);
                wc.environment(environment);
                wc.type(WorldType.FLAT);
                if (Version.SERVER_VERSION.isNewerEquals(Version.v1_13_R1)) {
                    wc.generator(WorldAdaptorImpl.this.plugin.getDescription().getName());
                } else {
                    wc.generatorSettings("2;0;1");
                }
                future.complete(wc.createWorld());
            });
            return future.join();
        });
    }

    @Override
    public ActionFuture<World> loadWorldFromFolder(String worldName, World.Environment environment) {
        return ActionFuture.supplyAsync(() -> {
            CompletableFuture<World> future = new CompletableFuture<>();
            SchedulerUtils.runTask(() -> {
                WorldCreator wc = new WorldCreator(worldName);
                wc.environment(environment);
                future.complete(wc.createWorld());
            });
            return future.join();
        });
    }

    @Override
    public ActionFuture<World> loadWorldFromSave(String fileName, String worldName, World.Environment environment) {
        ActionFuture<World> future = this.createWorld(worldName, environment);

        return future.thenApply(world -> {
            PluginFileUtils.copyWorldRegion(worldName, fileName);

            this.plugin.getNMSAdaptor().clearRegionFileCache(world);
            this.plugin.getNMSAdaptor().clearChunkCache(world);
            World finalWorld = world;
            this.plugin.getThreadHandler().runTaskLater(() -> {
                for (Player player : finalWorld.getPlayers()) {
                    plugin.getNMSAdaptor().refreshPlayerChunk(player);
                }
            }, Duration.ofSeconds(1));
            return world;
        });
    }

    @Override
    public boolean saveWorld(String worldName, String fileName) {
        this.unloadWorld(worldName, true);

        return PluginFileUtils.saveWorldRegions(worldName, fileName);
    }

    @Override
    public boolean saveExist(String name) {
        File file = new File(PluginFiles.Folder.ARENA_DATA_ARENACACHE, name + ".zip");
        return file.exists();
    }

    @Override
    public void unloadWorld(String worldName, boolean save) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            Bukkit.unloadWorld(world, save);
        }
    }

    @Override
    public void deleteWorld(String worldName) {
        for (File file : PluginFiles.Folder.ARENA_DATA_ARENACACHE.listFiles()) {
            if (file.getName().equals(worldName + ".zip")) file.delete();
        }
    }
}
