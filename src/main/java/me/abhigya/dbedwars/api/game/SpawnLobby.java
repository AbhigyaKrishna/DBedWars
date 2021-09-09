package me.abhigya.dbedwars.api.game;

import me.abhigya.dbedwars.api.util.LocationXYZYP;
import org.bukkit.World;

public interface SpawnLobby {

    World getWorld();

    void setWorld(World world);

    LocationXYZYP getSpawnLocation();

    void setSpawnLocation(LocationXYZYP location);

    boolean isTimeStatic();

    void setTimeStatic(boolean flag);

    long getTime();

    void setTime(long time);

}
