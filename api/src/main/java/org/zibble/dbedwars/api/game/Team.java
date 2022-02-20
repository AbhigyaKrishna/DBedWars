package org.zibble.dbedwars.api.game;

import com.google.common.collect.Multimap;
import com.pepedevs.radium.utils.math.collision.BoundingBox;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.api.game.trap.Trap;
import org.zibble.dbedwars.api.messaging.AbstractMessaging;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.LocationXYZ;
import org.zibble.dbedwars.api.util.LocationXYZYP;

import java.util.List;
import java.util.Set;

public interface Team extends AbstractMessaging {

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

    Arena getArena();

    void addPlayer(ArenaPlayer player);

    void removePlayer(ArenaPlayer player);

    Set<ArenaPlayer> getPlayers();

    boolean isBedBroken();

    void setBedBroken(boolean flag);

    boolean isEliminated();

    void setEliminated(boolean flag);

    BoundingBox getIslandArea();

    List<Trap> getTrapQueue();

    void spawnShopNpc(LocationXYZYP location);

    void spawnUpgradesNpc(LocationXYZYP location);

    String toString();
}
