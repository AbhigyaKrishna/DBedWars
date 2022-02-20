package org.zibble.dbedwars.api.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.game.view.ShopView;
import org.zibble.dbedwars.api.messaging.member.PlayerMember;
import org.zibble.dbedwars.api.objects.points.Points;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.Keyed;

import java.time.Instant;
import java.util.UUID;

public interface ArenaPlayer extends PlayerMember, Keyed<UUID> {

    Arena getArena();

    Team getTeam();

    void setTeam(Team team);

    Points getPoints();

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

    ShopView getShopView();

    String toString();

    class PlayerPoints {

        public static final Key<String> KILLS = Key.of("kills");
        public static final Key<String> DEATH = Key.of("death");
        public static final Key<String> BEDS = Key.of("beds");
        public static final Key<String> FINAL_KILLS = Key.of("final_kills");

    }

}
