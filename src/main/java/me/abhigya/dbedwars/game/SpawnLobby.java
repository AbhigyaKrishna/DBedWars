package me.abhigya.dbedwars.game;

import me.abhigya.dbedwars.api.util.LocationXYZYP;
import org.bukkit.World;

public class SpawnLobby implements me.abhigya.dbedwars.api.game.SpawnLobby {

    private World world;
    private LocationXYZYP spawn;
    private boolean timeStatic;
    private long time;

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public LocationXYZYP getSpawnLocation() {
        return this.spawn;
    }

    @Override
    public void setSpawnLocation(LocationXYZYP location) {
        this.spawn = location;
    }

    @Override
    public boolean isTimeStatic() {
        return this.timeStatic;
    }

    @Override
    public void setTimeStatic(boolean flag) {
        this.timeStatic = flag;
    }

    @Override
    public long getTime() {
        return this.time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }
}
