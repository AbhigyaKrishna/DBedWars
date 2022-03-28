package org.zibble.dbedwars.api.game;

import com.google.common.collect.Multimap;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.game.spawner.Spawner;
import org.zibble.dbedwars.api.game.trap.Trap;
import org.zibble.dbedwars.api.messaging.AbstractMessaging;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;

import java.util.List;
import java.util.Set;

public interface Team extends AbstractMessaging {

    Color getColor();

    String getName();

    LocationXYZ getBedLocation();

    void setBedLocation(LocationXYZ location);

    LocationXYZYP getSpawn();

    void setSpawn(LocationXYZYP location);

    Arena getArena();

    void addPlayer(ArenaPlayer player);

    void removePlayer(ArenaPlayer player);

    Set<ArenaPlayer> getPlayers();

    Set<Spawner> getSpawners();

    boolean isBedBroken();

    void setBedBroken(boolean flag);

    boolean isEliminated();

    void setEliminated(boolean flag);

    BoundingBox getIslandArea();

    List<Trap> getTrapQueue();

}
