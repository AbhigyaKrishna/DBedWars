package com.pepedevs.dbedwars.api.game;

import com.pepedevs.radium.utils.entity.UUIDPlayer;
import com.pepedevs.dbedwars.api.game.view.ShopView;
import com.pepedevs.dbedwars.api.messaging.member.PlayerMember;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Instant;

public interface ArenaPlayer extends PlayerMember {

    Arena getArena();

    void setArena(Arena arena);

    Team getTeam();

    void setTeam(Team team);

    boolean isSpectator();

    void setSpectator(boolean spectator);

    void addKill();

    void setKill(int count);

    int getKills();

    void addFinalKills();

    int getFinalKills();

    void setFinalKills(int count);

    void addDeath();

    int getDeath();

    void setDeath(int count);

    void addBedDestroy();

    short getBedDestroy();

    void setBedDestroy(short bedDestroy);

    void kill(DeathCause reason);

    boolean isFinalKilled();

    void setFinalKilled(boolean flag);

    UUIDPlayer getUUIDPlayer();

    String getName();

    Player getPlayer();

    void spawn(Location location);

    ArenaPlayer getLastHitTagged();

    void setLastHitTag(ArenaPlayer player);

    Instant getLastHitTime();

    void setLastHitTime(Instant instant);

    boolean isRespawning();

    ShopView getShopView();

    void queueRespawn();

    String toString();

}
