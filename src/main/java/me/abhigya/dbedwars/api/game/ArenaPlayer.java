package me.abhigya.dbedwars.api.game;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Instant;

public interface ArenaPlayer {

    void setArena(Arena arena);

    Arena getArena();

    void setTeam(Team team);

    Team getTeam();

    void setSpectator(boolean spectator);

    boolean isSpectator();

    void addKill();

    void setKill(int count);

    int getKills();

    void addFinalKills();

    void setFinalKills(int count);

    int getFinalKills();

    void addDeath();

    void setDeath(int count);

    int getDeath();

    void kill(DeathCause reason);

    boolean isFinalKilled();

    void setFinalKilled(boolean flag);

    Player getPlayer();

    void spawn(Location location);

    void sendMessage(String msg);

    void sendMessage(BaseComponent[] component);

    ArenaPlayer getLastHitTagged();

    void setLastHitTag(ArenaPlayer player);

    Instant getLastHitTime();

    void setLastHitTime(Instant instant);

    boolean isRespawning();

    void queueRespawn();

}
