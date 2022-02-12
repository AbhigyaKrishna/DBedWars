package com.pepedevs.dbedwars.api.hooks.world;

import org.bukkit.World;

public interface WorldAdaptor {

    World createWorld(String worldName, World.Environment environment);

    World loadWorldFromFolder(String worldName);

    World loadWorldFromSave(String fileName);

    boolean saveWorld(String worldName, String name);

    boolean saveExist(String name);

    void unloadWorld(String worldName, boolean save);

    void deleteWorld(String worldName);
}
