package com.pepedevs.dbedwars.task;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.RegenerationType;
import com.pepedevs.dbedwars.api.task.Regeneration;
import org.bukkit.World;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class WorldRegenerator extends Regeneration {

    private DBedwars plugin;

    public WorldRegenerator(DBedwars plugin, RegenerationType type, Arena arena) {
        super(type, arena);
        this.plugin = plugin;
    }

    @Override
    public Future<World> regenerate() {
        CompletableFuture<World> world = new CompletableFuture<>();
        switch (this.getRegenerationType()) {
            case SINGLE_THREADED:
                world.complete(
                        this.plugin
                                .getGeneratorHandler()
                                .getWorldAdaptor()
                                .loadWorldFromSave(this.getArena().getSettings().getName()));
                break;
            case MULTI_THREADED_ASYNC:
                this.plugin
                        .getThreadHandler()
                        .submitAsync(
                                () ->
                                        world.complete(
                                                this.plugin
                                                        .getGeneratorHandler()
                                                        .getWorldAdaptor()
                                                        .loadWorldFromSave(
                                                                this.getArena()
                                                                        .getSettings()
                                                                        .getName())));
                break;
            case MULTI_THREADED_SYNC:
                this.plugin
                        .getThreadHandler()
                        .submitSync(
                                () ->
                                        world.complete(
                                                this.plugin
                                                        .getGeneratorHandler()
                                                        .getWorldAdaptor()
                                                        .loadWorldFromSave(
                                                                this.getArena()
                                                                        .getSettings()
                                                                        .getName())));
                break;
        }
        return world;
    }
}
