package com.pepedevs.dbedwars.api.game;

import com.pepedevs.dbedwars.api.util.LocationXYZ;
import com.pepedevs.dbedwars.api.util.LocationXYZYP;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface ArenaSpectator {

    UUID getUUID();

    String getName();

    Arena getArena();

    Player getPlayer();

    void teleport(double x, double y, double z);

    void teleport(Location location);

    void teleport(LocationXYZ location);

    void teleport(LocationXYZYP location);

}
