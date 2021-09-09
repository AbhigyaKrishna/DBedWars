package me.abhigya.dbedwars.api.game.spawner;

import me.Abhigya.core.particle.particlelib.ParticleBuilder;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.Team;
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

    int getLevel();

    DropType.Tier getTier();

    Instant getLastUpgrade();

    ParticleBuilder getParticle();

    boolean exists();

    boolean remove();

}
