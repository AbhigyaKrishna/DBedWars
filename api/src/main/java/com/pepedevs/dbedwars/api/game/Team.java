package com.pepedevs.dbedwars.api.game;

import com.google.common.collect.Multimap;
import me.Abhigya.core.util.math.collision.BoundingBox;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import com.pepedevs.dbedwars.api.util.LocationXYZYP;
import com.pepedevs.dbedwars.api.util.TrapEnum;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;
import java.util.Set;

public interface Team {

    Color getColor();

    String getName();

    LocationXYZ getBedLocation();

    void setBedLocation(LocationXYZ location);

    LocationXYZYP getSpawn();

    void setSpawn(LocationXYZYP location);

    void addSpawner(DropType dropType, LocationXYZ location);

    Multimap<DropType, LocationXYZ> getSpawners();

    LocationXYZYP getShopNpc();

    void setShopNpc(LocationXYZYP location);

    LocationXYZYP getUpgradesNpc();

    void setUpgradesNpc(LocationXYZYP location);

    void reloadData();

    boolean isConfigured();

    void init(Arena arena);

    void registerTeam(Scoreboard scoreboard);

    Arena getArena();

    void addPlayer(ArenaPlayer player);

    void removePlayer(ArenaPlayer player);

    Set<ArenaPlayer> getPlayers();

    void sendMessage(String message);

    boolean isBedBroken();

    void setBedBroken(boolean flag);

    boolean isEliminated();

    void setEliminated(boolean flag);

    BoundingBox getIslandArea();

    List<Trap> getTrapQueue();

    void triggerTrap(TrapEnum.TriggerType trigger, ArenaPlayer target);

    void spawnShopNpc(LocationXYZYP location);

    void spawnUpgradesNpc(LocationXYZYP location);
}