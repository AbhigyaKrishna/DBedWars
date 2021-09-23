package me.abhigya.dbedwars.api.game;

import com.google.common.collect.Multimap;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.Color;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import me.abhigya.dbedwars.api.util.LocationXYZYP;

import java.util.Set;

public interface Team {

    Color getColor();

    String getName();

    void setBedLocation(LocationXYZ location);

    LocationXYZ getBedLocation();

    void setSpawn(LocationXYZYP location);

    LocationXYZYP getSpawn();

    void addSpawner(DropType dropType, LocationXYZ location);

    Multimap<DropType, LocationXYZ> getSpawners();

    void setShopNpc(LocationXYZYP location);

    LocationXYZYP getShopNpc();

    void setUpgradesNpc(LocationXYZYP location);

    LocationXYZYP getUpgradesNpc();

    void reloadData();

    boolean isConfigured();

    void init(Arena arena);

    Arena getArena();

    void addPlayer(ArenaPlayer player);

    void removePlayer(ArenaPlayer player);

    Set<ArenaPlayer> getPlayers();

    void sendMessage(String message);

    boolean isBedBroken();

    void setBedBroken(boolean flag);

    boolean isEliminated();

    void setEliminated(boolean flag);

    void spawnShopNpc(LocationXYZYP location);

    void spawnUpgradesNpc(LocationXYZYP location);
}
