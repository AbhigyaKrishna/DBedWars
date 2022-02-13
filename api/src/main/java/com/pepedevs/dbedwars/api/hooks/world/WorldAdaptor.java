package com.pepedevs.dbedwars.api.hooks.world;

import com.pepedevs.dbedwars.api.future.ActionFuture;
import org.bukkit.World;

public interface WorldAdaptor {

    ActionFuture<World> createWorld(String worldName, World.Environment environment);

    ActionFuture<World> loadWorldFromFolder(String worldName);

    ActionFuture<World> loadWorldFromSave(String fileName);

    boolean saveWorld(String worldName, String name);

    boolean saveExist(String name);

    void unloadWorld(String worldName, boolean save);

    void deleteWorld(String worldName);
}
