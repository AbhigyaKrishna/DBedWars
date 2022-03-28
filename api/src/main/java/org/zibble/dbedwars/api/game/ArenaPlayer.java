package org.zibble.dbedwars.api.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.game.statistics.ResourceStatistics;
import org.zibble.dbedwars.api.hooks.scoreboard.Scoreboard;
import org.zibble.dbedwars.api.objects.points.Points;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Key;

import java.time.Instant;
import java.util.UUID;

public interface ArenaPlayer extends ArenaSpectator {

    Arena getArena();

    Team getTeam();

    void setTeam(Color team);

    Points getPoints();

    Scoreboard getScoreboard();

    void setScoreboard(Scoreboard scoreboard);

    void kill(DeathCause reason);

    boolean isFinalKilled();

    void setFinalKilled(boolean flag);

    UUID getUUID();

    String getName();

    Player getPlayer();

    void spawn(Location location);

    boolean isHitTagged();

    ArenaPlayer getLastHitTagged();

    void setLastHitTag(ArenaPlayer player, Instant instant);

    boolean isRespawning();

    void setRespawning(boolean flag);

    String toString();

    ResourceStatistics getResourceStatistics();

    class PlayerPoints {

        public static final Key<String> KILLS = Key.of("kills");
        public static final Key<String> DEATH = Key.of("death");
        public static final Key<String> BEDS = Key.of("beds");
        public static final Key<String> FINAL_KILLS = Key.of("final_kills");

    }

}
