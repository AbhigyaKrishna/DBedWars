package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.api.util.properies.PropertySerializable;
import org.zibble.dbedwars.configuration.framework.Configurable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;

import java.util.List;
import java.util.Map;

public class ConfigurableArena implements Configurable, PropertySerializable {

    @ConfigPath("name")
    private String identifier;

    @ConfigPath("enabled")
    private boolean enabled;

    @ConfigPath("world-environment")
    private World.Environment environment;

    @ConfigPath("icon")
    private String icon;

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

    @ConfigPath("customName")
    private String customName;

    @ConfigPath("teams")
    private Map<String, ConfigurableTeam> teams;

    @ConfigPath("spawners")
    private List<String> spawners;

    @ConfigPath("override")
    private ConfigurableArenaOverride override;

    public ConfigurableArena() {
    }

    public ConfigurableArena(Arena arena) {
    }

    @Override
    public void load(ConfigurationSection section) {
        this.teams.clear();
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLobbyLoc() {
        return lobbyLoc;
    }

    public void setLobbyLoc(String lobbyLoc) {
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

    public ConfigurableArenaOverride getOverride() {
        return override;
    }

    public void setOverride(ConfigurableArenaOverride override) {
        this.override = override;
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

        @ConfigPath("color")
        private Color color;

        @ConfigPath("bed")
        private String bedLocation;

        @ConfigPath("spawn")
        private String spawn;

        @ConfigPath("shop")
        private String shopNpc;

        @ConfigPath("upgrades")
        private String upgradesNpc;

        @ConfigPath("spawners")
        public List<String> spawners;

        public ConfigurableTeam() {
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

        public String getShopNpc() {
            return shopNpc;
        }

        public void setShopNpc(String shopNpc) {
            this.shopNpc = shopNpc;
        }

        public String getUpgradesNpc() {
            return upgradesNpc;
        }

        public void setUpgradesNpc(String upgradesNpc) {
            this.upgradesNpc = upgradesNpc;
        }

    }

}
