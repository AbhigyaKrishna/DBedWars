package me.abhigya.dbedwars.game.arena.settings;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.abhigya.dbedwars.api.game.Arena;
import me.abhigya.dbedwars.api.game.RegenerationType;
import me.abhigya.dbedwars.api.game.Team;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.Color;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import me.abhigya.dbedwars.api.util.LocationXYZYP;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableArena;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ArenaSettings implements me.abhigya.dbedwars.api.game.settings.ArenaSettings {

    private final Arena arena;

    private String name;
    private String customName;
    private World.Environment worldEnv;
    private RegenerationType regenerationType;
    private LocationXYZYP lobby;
    private LocationXYZYP spectatorLocation;
    private LocationXYZ lobbyPosMax;
    private LocationXYZ lobbyPosMin;
    private BwItemStack icon;
    private int teamPlayers;
    private int minPlayers;

    private Multimap<DropType, LocationXYZ> drops;
    private Set<Team> availableTeams;

    public ArenaSettings(Arena arena) {
        this.arena = arena;
        this.teamPlayers = 1;
        this.worldEnv = World.Environment.NORMAL;
        this.regenerationType = RegenerationType.MULTI_THREADED_SYNC;
        this.drops = ArrayListMultimap.create();
        this.availableTeams = new HashSet<>();
    }

    public ArenaSettings(Arena arena, ConfigurableArena cfgArena) {
        this(arena);
        this.update(cfgArena);
    }

    @Override
    public Arena getArena() {
        return this.arena;
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
    public RegenerationType getRegenerationType() {
        return this.regenerationType;
    }

    @Override
    public void setRegenerationType(RegenerationType regenerationType) {
        this.regenerationType = regenerationType;
    }

    @Override
    public LocationXYZYP getLobby() {
        return this.lobby;
    }

    @Override
    public boolean hasLobby() {
        return this.lobby != null;
    }

    @Override
    public void setLobby(LocationXYZYP lobby) {
        this.lobby = lobby;
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
        this.availableTeams.add(new me.abhigya.dbedwars.game.arena.Team(teamColor));
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
            if (t.getColor().equals(teamColor))
                return t;
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

    public void update(ConfigurableArena cfgArena) {
        this.name = cfgArena.getIdentifier();
        this.customName = cfgArena.getCustomName();
        this.worldEnv = cfgArena.getWorldEnv();
        this.icon = cfgArena.getIcon();
        this.lobby = cfgArena.getLobbyLoc();
        this.lobbyPosMax = cfgArena.getLobbyPosMax();
        this.lobbyPosMin = cfgArena.getLobbyPosMin();
        this.spectatorLocation = cfgArena.getSpectatorLocation();
        this.teamPlayers = cfgArena.getPlayerInTeam();
        this.minPlayers = cfgArena.getMinPlayers();
        this.regenerationType = cfgArena.getRegenerationType();
        this.availableTeams = new HashSet<>(cfgArena.getTeams());
        this.drops = cfgArena.getSpawners();
    }
}
