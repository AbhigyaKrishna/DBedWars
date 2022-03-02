package org.zibble.dbedwars.api.hooks.world;

import org.bukkit.World;
import org.zibble.dbedwars.api.future.ActionFuture;

public interface WorldAdaptor {

    ActionFuture<World> createWorld(String worldName, World.Environment environment);

    ActionFuture<World> loadWorldFromFolder(String worldName, World.Environment environment);

    ActionFuture<World> loadWorldFromSave(String fileName, String worldName, World.Environment environment);

    boolean saveWorld(String worldName, String name);

    boolean saveExist(String name);

    void unloadWorld(String worldName, boolean save);

    void deleteWorld(String worldName);
}
