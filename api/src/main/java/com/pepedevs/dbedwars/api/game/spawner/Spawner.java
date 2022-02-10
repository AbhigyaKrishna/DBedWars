package com.pepedevs.dbedwars.api.game.spawner;

import com.pepedevs.dbedwars.api.util.Keyed;
import com.pepedevs.radium.particles.ParticleBuilder;
import com.pepedevs.dbedwars.api.util.Initializable;
import com.pepedevs.dbedwars.api.util.Tickable;
import com.pepedevs.radium.utils.math.collision.BoundingBox;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;
import org.bukkit.Location;

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
