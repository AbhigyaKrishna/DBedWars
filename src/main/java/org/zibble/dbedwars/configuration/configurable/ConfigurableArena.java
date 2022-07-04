package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.api.util.properies.PropertySerializable;
import org.zibble.dbedwars.configuration.framework.Configurable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.configuration.framework.annotations.Defaults;
import org.zibble.dbedwars.game.ArenaDataHolderImpl;
import org.zibble.dbedwars.utils.ConfigurationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurableArena implements Configurable, PropertySerializable {

    private static final World.Environment DEF_ENVIRONMENT = World.Environment.NORMAL;

    @ConfigPath("name")
    private String identifier;

    @Defaults.Boolean(false)
    @ConfigPath
    private boolean enabled;

    @Defaults.Variable("DEF_ENVIRONMENT")
    @ConfigPath("world-environment")
    private World.Environment environment;

    @ConfigPath
    private String icon;

    @ConfigPath
    private String category;

    @ConfigPath("lobby.location")
    private String lobbyLoc;

    @ConfigPath("lobby.corner1")
    private String lobbyPosMax;

    @ConfigPath("lobby.corner2")
    private String lobbyPosMin;

    @ConfigPath("spectatorLoc")
    private String spectatorLocation;

    @ConfigPath("playersInTeam")
    private int playerInTeam;

    @ConfigPath("minPlayersToStart")
    private int minPlayers;

    @ConfigPath
    private String customName;

    @ConfigPath
    private String scoreboard;

    @ConfigPath
    private Map<String, ConfigurableTeam> teams;

    @ConfigPath
    private List<String> spawners;

    public ConfigurableArena() {
    }

    public ConfigurableArena(ArenaDataHolderImpl dataHolder) {
        this.identifier = dataHolder.getId();
        this.enabled = dataHolder.isEnabled();
        this.environment = dataHolder.getEnvironment();
//        this.icon = dataHolder.getIcon(); // TODO: 15-04-2022 icon
        this.category = dataHolder.getCategory() == null ? null : dataHolder.getCategory().getName();
        this.lobbyLoc = dataHolder.getWaitingLocation() == null ? null : dataHolder.getWaitingLocation().toString();
        this.lobbyPosMax = dataHolder.getLobbyArea() == null ? null : LocationXYZ.valueOf(dataHolder.getLobbyArea().getMaximum()).toString();
        this.lobbyPosMin = dataHolder.getLobbyArea() == null ? null : LocationXYZ.valueOf(dataHolder.getLobbyArea().getMinimum()).toString();
        this.spectatorLocation = dataHolder.getSpectatorLocation() == null ? null : dataHolder.getSpectatorLocation().toString();
        this.playerInTeam = dataHolder.getMaxPlayersPerTeam();
        this.minPlayers = dataHolder.getMinPlayersToStart();
        this.customName = dataHolder.getCustomName() == null ? null : dataHolder.getCustomName().getMessage();
        this.spawners = new ArrayList<>(dataHolder.getSpawners().size());
        for (ArenaDataHolderImpl.SpawnerDataHolderImpl spawner : dataHolder.getSpawners()) {
            this.spawners.add(ConfigurationUtil.serializeSpawner(spawner.getDropType(), spawner.getLocation()));
        }
        this.teams = new HashMap<>(dataHolder.getTeamData().size());
        for (Map.Entry<Color, ArenaDataHolderImpl.TeamDataHolderImpl> entry : dataHolder.getTeamData().entrySet()) {
            this.teams.put(entry.getKey().name(), new ConfigurableTeam(entry.getValue()));
        }
    }

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return this.identifier != null;
    }

    @Override
    public int save(ConfigurationSection section) {
        return this.saveEntries(section);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public World.Environment getEnvironment() {
        return environment;
    }

    public void setWorldEnv(World.Environment worldEnv) {
        this.environment = worldEnv;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLobbyLocation() {
        return lobbyLoc;
    }

    public void setLobbyLocation(String lobbyLoc) {
        this.lobbyLoc = lobbyLoc;
    }

    public String getLobbyPosMax() {
        return lobbyPosMax;
    }

    public void setLobbyPosMax(String lobbyPosMax) {
        this.lobbyPosMax = lobbyPosMax;
    }

    public String getLobbyPosMin() {
        return lobbyPosMin;
    }

    public void setLobbyPosMin(String lobbyPosMin) {
        this.lobbyPosMin = lobbyPosMin;
    }

    public String getSpectatorLocation() {
        return spectatorLocation;
    }

    public void setSpectatorLocation(String spectatorLocation) {
        this.spectatorLocation = spectatorLocation;
    }

    public int getPlayerInTeam() {
        return playerInTeam;
    }

    public void setPlayerInTeam(int playerInTeam) {
        this.playerInTeam = playerInTeam;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(String scoreboard) {
        this.scoreboard = scoreboard;
    }

    public Map<String, ConfigurableTeam> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, ConfigurableTeam> teams) {
        this.teams = teams;
    }

    public List<String> getSpawners() {
        return spawners;
    }

    public void setSpawners(List<String> spawners) {
        this.spawners = spawners;
    }

    @Override
    public String toString() {
        return "ConfigurableArena{" +
                "identifier='" + identifier + '\'' +
                ", enabled=" + enabled +
                ", environment=" + environment +
                ", icon='" + icon + '\'' +
                ", category='" + category + '\'' +
                ", lobbyLoc='" + lobbyLoc + '\'' +
                ", lobbyPosMax='" + lobbyPosMax + '\'' +
                ", lobbyPosMin='" + lobbyPosMin + '\'' +
                ", spectatorLocation='" + spectatorLocation + '\'' +
                ", playerInTeam=" + playerInTeam +
                ", minPlayers=" + minPlayers +
                ", customName='" + customName + '\'' +
                ", scoreboard='" + scoreboard + '\'' +
                ", teams=" + teams +
                ", spawners=" + spawners +
                '}';
    }

    @Override
    public NamedProperties toProperties() {
        return NamedProperties
                .builder()
                .add("name", identifier)
                .add("enabled", enabled)
                .add("world-environment", this.environment)
                .add("icon", icon)
                .add("lobby", NamedProperties.builder()
                        .add("location", LocationXYZYP.valueOf(lobbyLoc))
                        .add("corner1", LocationXYZ.valueOf(lobbyPosMax))
                        .add("corner2", LocationXYZ.valueOf(lobbyPosMin))
                        .build()
                )
                .add("spectatorLocation", LocationXYZYP.valueOf(spectatorLocation))
                .add("playersInTeam", playerInTeam)
                .add("minPlayersToStart", minPlayers)
                .add("customName", customName)
                .add("teams", teams)
                .add("spawners", spawners)
                .build();
    }

    public static class ConfigurableTeam implements Configurable {

        @ConfigPath
        private List<String> spawners;

        @ConfigPath
        private Color color;

        @ConfigPath("bed")
        private String bedLocation;

        @ConfigPath
        private String spawn;

        @ConfigPath
        private List<String> npcs;

        public ConfigurableTeam() {
        }

        public ConfigurableTeam(ArenaDataHolderImpl.TeamDataHolderImpl dataHolder) {
            this.spawners = new ArrayList<>(dataHolder.getSpawners().size());
            for (ArenaDataHolderImpl.SpawnerDataHolderImpl spawner : dataHolder.getSpawners()) {
                this.spawners.add(ConfigurationUtil.serializeSpawner(spawner.getDropType(), spawner.getLocation()));
            }

            this.color = dataHolder.getColor();
            this.bedLocation = dataHolder.getBed() == null ? null : dataHolder.getBed().toString();
            this.spawn = dataHolder.getSpawnLocation() == null ? null : dataHolder.getSpawnLocation().toString();
        }

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public int save(ConfigurationSection section) {
            return this.saveEntries(section);
        }

        public List<String> getSpawners() {
            return spawners;
        }

        public void setSpawners(List<String> spawners) {
            this.spawners = spawners;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public String getBedLocation() {
            return bedLocation;
        }

        public void setBedLocation(String bedLocation) {
            this.bedLocation = bedLocation;
        }

        public String getSpawn() {
            return spawn;
        }

        public void setSpawn(String spawn) {
            this.spawn = spawn;
        }

        public List<String> getNpcs() {
            return npcs;
        }

        @Override
        public String toString() {
            return "ConfigurableTeam{" +
                    "spawners=" + spawners +
                    ", color=" + color +
                    ", bedLocation='" + bedLocation + '\'' +
                    ", spawn='" + spawn + '\'' +
                    ", npcs=" + npcs +
                    '}';
        }

    }

}
