package com.pepedevs.dbedwars.configuration.configurable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pepedevs.corelib.utils.configuration.Configurable;
import com.pepedevs.corelib.utils.configuration.Loadable;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableCollectionEntry;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableEntry;
import com.pepedevs.corelib.utils.configuration.annotations.SaveableCollectionEntry;
import com.pepedevs.corelib.utils.configuration.annotations.SaveableEntry;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import com.pepedevs.dbedwars.api.util.LocationXYZYP;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurableArena implements Configurable {

    private Arena arena;

    @SaveableEntry(key = "name")
    @LoadableEntry(key = "name")
    private String identifier;

    @SaveableEntry(key = "enabled")
    @LoadableEntry(key = "enabled")
    private boolean enabled;

    @SaveableEntry(key = "world-environment")
    @LoadableEntry(key = "world-environment")
    private String worldEnv;

    @SaveableEntry(key = "icon")
    @LoadableEntry(key = "icon")
    private String icon;

    @SaveableEntry(key = "lobby.location")
    @LoadableEntry(key = "lobby.location")
    private String lobbyLoc;

    @SaveableEntry(key = "lobby.corner1")
    @LoadableEntry(key = "lobby.corner1")
    private String lobbyPosMax;

    @SaveableEntry(key = "lobby.corner2")
    @LoadableEntry(key = "lobby.corner2")
    private String lobbyPosMin;

    @SaveableEntry(key = "spectatorLoc")
    @LoadableEntry(key = "spectatorLoc")
    private String spectatorLocation;

    @SaveableEntry(key = "playersInTeam")
    @LoadableEntry(key = "playersInTeam")
    private int playerInTeam;

    @SaveableEntry(key = "minPlayersToStart")
    @LoadableEntry(key = "minPlayersToStart")
    private int minPlayers;

    @SaveableEntry(key = "customName")
    @LoadableEntry(key = "customName")
    private String customName;

    @SaveableCollectionEntry(subsection = "teams")
    @LoadableCollectionEntry(subsection = "teams")
    private List<ConfigurableTeam> teams;

    @SaveableEntry(key = "spawners")
    @LoadableEntry(key = "spawners")
    private List<String> spawners;

    private ConfigurableArenaOverride override;

    public ConfigurableArena() {
        this.spawners = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public ConfigurableArena(Arena arena) {
        this.arena = arena;
        this.update();
    }

    @Override
    public Loadable load(ConfigurationSection section) {
        this.teams.clear();
        this.override = new ConfigurableArenaOverride(DBedwars.getInstance());
        this.override.load(section);
        return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return this.identifier != null;
    }

    @Override
    public boolean isInvalid() {
        return !this.isValid();
    }

    @Override
    public int save(ConfigurationSection section) {
        return this.saveEntries(section);
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public World.Environment getWorldEnv() {
        return World.Environment.valueOf(this.worldEnv);
    }

    public BwItemStack getIcon() {
        return this.icon != null ? new BwItemStack(Material.valueOf(this.icon)) : null;
    }

    public LocationXYZYP getSpectatorLocation() {
        return this.spectatorLocation != null
                ? LocationXYZYP.valueOf(this.spectatorLocation)
                : null;
    }

    public int getPlayerInTeam() {
        return playerInTeam;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public String getCustomName() {
        return customName;
    }

    public LocationXYZYP getLobbyLoc() {
        return this.lobbyLoc == null ? null : LocationXYZYP.valueOf(this.lobbyLoc);
    }

    public LocationXYZ getLobbyPosMax() {
        return this.lobbyPosMax != null ? LocationXYZ.valueOf(this.lobbyPosMax) : null;
    }

    public LocationXYZ getLobbyPosMin() {
        return this.lobbyPosMin != null ? LocationXYZ.valueOf(this.lobbyPosMin) : null;
    }

    public List<Team> getTeams() {
        return this.teams.stream().map(ConfigurableTeam::toTeam).collect(Collectors.toList());
    }

    public Multimap<DropType, LocationXYZ> getSpawners() {
        Multimap<DropType, LocationXYZ> multimap = ArrayListMultimap.create();
        for (String s : this.spawners) {
            Map.Entry<DropType, LocationXYZ> entry = ConfigurationUtils.parseSpawner(s);
            if (entry != null) multimap.put(entry.getKey(), entry.getValue());
        }

        return multimap;
    }

    public void update() {
        if (this.arena == null)
            throw new IllegalStateException("Arena is null somehow in the configuration!");

        this.identifier = this.arena.getSettings().getName();
        this.enabled = this.arena.isEnabled();
        this.worldEnv = this.arena.getSettings().getWorldEnv().name();
        this.icon =
                this.arena.getSettings().getIcon() != null
                        ? this.arena.getSettings().getIcon().getType().name()
                        : null;
        this.lobbyLoc =
                this.arena.getSettings().getLobby() != null
                        ? this.arena.getSettings().getLobby().toString()
                        : null;
        this.lobbyPosMax =
                this.arena.getSettings().getLobbyPosMax() != null
                        ? this.arena.getSettings().getLobbyPosMax().toString()
                        : null;
        this.lobbyPosMin =
                this.arena.getSettings().getLobbyPosMin() != null
                        ? this.arena.getSettings().getLobbyPosMin().toString()
                        : null;
        this.spectatorLocation =
                this.arena.getSettings().getSpectatorLocation() != null
                        ? this.arena.getSettings().getSpectatorLocation().toString()
                        : null;
        this.minPlayers = this.arena.getSettings().getMinPlayers();
        this.playerInTeam = this.arena.getSettings().getTeamPlayers();
        this.customName = this.arena.getSettings().getCustomName();
        this.teams =
                this.arena.getSettings().getAvailableTeams() != null
                        ? this.arena.getSettings().getAvailableTeams().stream()
                                .map(ConfigurableTeam::new)
                                .collect(Collectors.toList())
                        : new ArrayList<>();
        this.spawners =
                this.arena.getSettings().getDrops() != null
                        ? this.arena.getSettings().getDrops().entries().stream()
                                .map(
                                        e ->
                                                ConfigurationUtils.serializeSpawner(
                                                        e.getKey(), e.getValue()))
                                .collect(Collectors.toList())
                        : new ArrayList<>();
    }

    public Arena toArena() {
        if (this.arena == null) {
            this.arena = new com.pepedevs.dbedwars.game.arena.Arena(DBedwars.getInstance(), this);
            if (this.override != null) this.override.apply(this.arena);
        }

        return this.arena;
    }
}
