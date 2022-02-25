package org.zibble.dbedwars.game.arena.settings;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.game.settings.ArenaSettings;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.game.arena.TeamImpl;

import java.util.*;

public class ArenaSettingsImpl implements ArenaSettings {

    private String name;
    private String customName;
    private World.Environment worldEnv;
    private LocationXYZYP lobby;
    private LocationXYZYP spectatorLocation;
    private LocationXYZ lobbyPosMax;
    private LocationXYZ lobbyPosMin;
    private BwItemStack icon;
    private int teamPlayers;
    private int minPlayers;

    /* Overridable settings */
    private int startTimer;
    private int respawnTime;
    private int islandRadius;
    private int minYAxis;
    private int playerHitTagLength;
    private int gameEndDelay;
    private boolean disableHunger;
    // Points
    private int bedDestroyPoint;
    private int killPoint;
    private int finalKillPoint;
    private int deathPoint;

    private Multimap<DropType, LocationXYZ> drops;
    private Set<Team> availableTeams;

    public ArenaSettingsImpl() {
        this.teamPlayers = 1;
        this.worldEnv = World.Environment.NORMAL;
        this.drops = ArrayListMultimap.create();
        this.availableTeams = new HashSet<>();
    }

    public ArenaSettingsImpl(NamedProperties properties) {
        this();
        this.startTimer = properties.getValue("startTimer");
        this.respawnTime = properties.getValue("respawnTime");
        this.islandRadius = properties.getValue("islandRadius");
        this.minYAxis = properties.getValue("minYAxis");
        this.playerHitTagLength = properties.getValue("playerHitTagLength");
        this.gameEndDelay = properties.getValue("gameEndDelay");
        this.disableHunger = properties.getValue("disableHunger", true);
        this.bedDestroyPoint = properties.getValue("bedDestroyPoint");
        this.killPoint = properties.getValue("killPoint");
        this.finalKillPoint = properties.getValue("finalKillPoint");
        this.deathPoint = properties.getValue("deathPoint");
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCustomName() {
        return this.customName;
    }

    @Override
    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Override
    public World.Environment getWorldEnv() {
        return this.worldEnv;
    }

    @Override
    public void setWorldEnv(World.Environment worldEnv) {
        this.worldEnv = worldEnv;
    }

    @Override
    public LocationXYZYP getLobby() {
        return this.lobby;
    }

    @Override
    public void setLobby(LocationXYZYP lobby) {
        this.lobby = lobby;
    }

    @Override
    public boolean hasLobby() {
        return this.lobby != null;
    }

    @Override
    public LocationXYZYP getSpectatorLocation() {
        return this.spectatorLocation;
    }

    @Override
    public void setSpectatorLocation(LocationXYZYP spectatorLocation) {
        this.spectatorLocation = spectatorLocation;
    }

    @Override
    public LocationXYZ getLobbyPosMax() {
        return this.lobbyPosMax;
    }

    @Override
    public void setLobbyPosMax(LocationXYZ location) {
        this.lobbyPosMax = location;
    }

    @Override
    public LocationXYZ getLobbyPosMin() {
        return this.lobbyPosMin;
    }

    @Override
    public void setLobbyPosMin(LocationXYZ location) {
        this.lobbyPosMin = location;
    }

    @Override
    public BwItemStack getIcon() {
        return this.icon;
    }

    @Override
    public void setIcon(BwItemStack icon) {
        this.icon = icon;
    }

    @Override
    public int getTeamPlayers() {
        return this.teamPlayers;
    }

    @Override
    public void setTeamPlayers(int teamPlayers) {
        this.teamPlayers = teamPlayers;
    }

    @Override
    public int getMinPlayers() {
        return this.minPlayers;
    }

    @Override
    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    @Override
    public int getMaxPlayer() {
        return this.teamPlayers * this.availableTeams.size();
    }

    @Override
    public Multimap<DropType, LocationXYZ> getDrops() {
        return this.drops;
    }

    @Override
    public void addDrop(DropType dropType, LocationXYZ location) {
        this.drops.put(dropType.clone(), location);
    }

    @Override
    public void removeDrop(LocationXYZ location) {
        for (Map.Entry<DropType, LocationXYZ> d : this.drops.entries()) {
            if (d.getValue().equals(location)) {
                this.drops.remove(d.getKey(), d.getValue());
            }
        }
    }

    @Override
    public void removeDrop(DropType dropType, LocationXYZ location) {
        this.drops.remove(dropType, location);
    }

    @Override
    public Collection<LocationXYZ> getDropLocation(DropType dropType) {
        return this.drops.get(dropType);
    }

    @Override
    public Set<Team> getAvailableTeams() {
        return Collections.unmodifiableSet(this.availableTeams);
    }

    @Override
    public void enableTeam(Color teamColor) {
        this.availableTeams.add(new TeamImpl(DBedwars.getInstance(), teamColor));
    }

    @Override
    public void enableTeam(Team team) {
        this.availableTeams.add(team);
    }

    @Override
    public boolean isEnabled(Color teamColor) {
        return this.availableTeams.stream().anyMatch(t -> t.getColor().equals(teamColor));
    }

    @Nullable
    @Override
    public Team getTeam(Color teamColor) {
        for (Team t : this.availableTeams) {
            if (t.getColor().equals(teamColor)) return t;
        }

        return null;
    }

    @Override
    public void disableTeam(Color teamColor) {
        this.availableTeams.removeIf(team -> team.getColor().equals(teamColor));
    }

    @Override
    public void disableTeam(Team team) {
        this.availableTeams.remove(team);
    }

    @Override
    public int getStartTimer() {
        return startTimer;
    }

    @Override
    public void setStartTimer(int startTimer) {
        this.startTimer = startTimer;
    }

    @Override
    public int getRespawnTime() {
        return respawnTime;
    }

    @Override
    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    @Override
    public int getIslandRadius() {
        return islandRadius;
    }

    @Override
    public void setIslandRadius(int islandRadius) {
        this.islandRadius = islandRadius;
    }

    @Override
    public int getMinYAxis() {
        return minYAxis;
    }

    @Override
    public void setMinYAxis(int minYAxis) {
        this.minYAxis = minYAxis;
    }

    @Override
    public int getPlayerHitTagLength() {
        return playerHitTagLength;
    }

    @Override
    public void setPlayerHitTagLength(int playerHitTagLength) {
        this.playerHitTagLength = playerHitTagLength;
    }

    @Override
    public int getGameEndDelay() {
        return gameEndDelay;
    }

    @Override
    public void setGameEndDelay(int gameEndDelay) {
        this.gameEndDelay = gameEndDelay;
    }

    @Override
    public boolean isDisableHunger() {
        return disableHunger;
    }

    @Override
    public void setDisableHunger(boolean flag) {
        this.disableHunger = flag;
    }

    @Override
    public int getBedDestroyPoint() {
        return bedDestroyPoint;
    }

    @Override
    public void setBedDestroyPoint(int bedDestroyPoint) {
        this.bedDestroyPoint = bedDestroyPoint;
    }

    @Override
    public int getKillPoint() {
        return killPoint;
    }

    @Override
    public void setKillPoint(int killPoint) {
        this.killPoint = killPoint;
    }

    @Override
    public int getFinalKillPoint() {
        return finalKillPoint;
    }

    @Override
    public void setFinalKillPoint(int finalKillPoint) {
        this.finalKillPoint = finalKillPoint;
    }

    @Override
    public int getDeathPoint() {
        return deathPoint;
    }

    @Override
    public void setDeathPoint(int deathPoint) {
        this.deathPoint = deathPoint;
    }

}
