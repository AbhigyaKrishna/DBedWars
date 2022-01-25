package com.pepedevs.dbedwars.api.game.spawner;

import com.pepedevs.radium.particles.ParticleBuilder;
import com.pepedevs.radium.utils.math.collision.BoundingBox;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;
import org.bukkit.Location;

import java.time.Instant;

public interface Spawner {

    void init();

    void tick();

    void spawn(DropType.Drop drop);

    boolean upgrade(int level);

    DropType getDropType();

    Instant getStartTime();

    Location getLocation();

    void setLocation(Location location);

    Arena getArena();

    Team getTeam();

    BoundingBox getBoundingBox();

    int getLevel();

    DropType.Tier getTier();

    Instant getLastUpgrade();

    ParticleBuilder getParticle();

    boolean exists();

    boolean remove();

    String toString();
}
