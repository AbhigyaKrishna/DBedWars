package org.zibble.dbedwars.api.game.spawner;

import org.bukkit.Location;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.util.Keyed;
import org.zibble.dbedwars.api.util.Tickable;

import java.time.Instant;
import java.util.Optional;

public interface Spawner extends Tickable, Keyed<DropType> {

    void spawn(DropType.Drop drop);

    boolean upgrade(int level);

    DropType getDropType();

    Instant getStartTime();

    Location getLocation();

    void setLocation(Location location);

    Arena getArena();

    Optional<Team> getTeam();

    BoundingBox getBoundingBox();

    DropType.Tier getTier();

    Instant getLastUpgrade();

    boolean remove();

    String toString();
}
