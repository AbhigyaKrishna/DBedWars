package me.abhigya.dbedwars.api.game.settings;

import com.google.common.collect.Multimap;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.RegenerationType;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.Color;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import me.abhigya.dbedwars.api.util.LocationXYZYP;
import org.bukkit.World;

import java.util.Collection;
import java.util.Set;

public interface ArenaSettings {

    Arena getArena();

    String getName();

    void setName(String name);

    String getCustomName();

    void setCustomName(String customName);

    boolean hasCustomName();

    World.Environment getWorldEnv();

    void setWorldEnv(World.Environment worldEnv);

    RegenerationType getRegenerationType();

    void setRegenerationType(RegenerationType regenerationType);

    LocationXYZYP getLobby();

    boolean hasLobby();

    void setLobby(LocationXYZYP lobby);

    LocationXYZYP getSpectatorLocation();

    void setSpectatorLocation(LocationXYZYP spectatorLocation);

    LocationXYZ getLobbyPosMax();

    void setLobbyPosMax(LocationXYZ location);

    LocationXYZ getLobbyPosMin();

    void setLobbyPosMin(LocationXYZ location);

    BwItemStack getIcon();

    void setIcon(BwItemStack icon);

    int getTeamPlayers();

    void setTeamPlayers(int teamPlayers);

    int getMinPlayers();

    void setMinPlayers(int minPlayers);

    int getMaxPlayer();

    Multimap<DropType, LocationXYZ> getDrops();

    void addDrop(DropType dropType, LocationXYZ location);

    void removeDrop(LocationXYZ location);

    void removeDrop(DropType dropType, LocationXYZ location);

    Collection<LocationXYZ> getDropLocation(DropType dropType);

    Set<Team> getAvailableTeams();

    void enableTeam(Color teamColor);

    void enableTeam(Team team);

    boolean isEnabled(Color teamColor);

    Team getTeam(Color teamColor);

    void disableTeam(Color teamColor);

    void disableTeam(Team team);

    int getStartTimer();

    void setStartTimer(int startTimer);

    int getRespawnTime();

    void setRespawnTime(int respawnTime);

    int getIslandRadius();

    void setIslandRadius(int islandRadius);

    int getMinYAxis();

    void setMinYAxis(int minYAxis);

    int getPlayerHitTagLength();

    void setPlayerHitTagLength(int playerHitTagLength);

    int getGameEndDelay();

    void setGameEndDelay(int gameEndDelay);

    boolean isDisableHunger();

    void setDisableHunger(boolean flag);

    int getBedDestroyPoint();

    void setBedDestroyPoint(int bedDestroyPoint);

    int getKillPoint();

    void setKillPoint(int killPoint);

    int getFinalKillPoint();

    void setFinalKillPoint(int finalKillPoint);

    int getDeathPoint();

    void setDeathPoint(int deathPoint);

}
