package com.pepedevs.dbedwars.api.game;

import me.Abhigya.core.util.entity.UUIDPlayer;
import com.pepedevs.dbedwars.api.game.view.ShopView;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Instant;

public interface ArenaPlayer {

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

    void sendMessage(String msg);

    void sendMessage(BaseComponent[] component);

    ArenaPlayer getLastHitTagged();

    void setLastHitTag(ArenaPlayer player);

    Instant getLastHitTime();

    void setLastHitTime(Instant instant);

    boolean isRespawning();

    ShopView getShopView();

    void queueRespawn();
}
