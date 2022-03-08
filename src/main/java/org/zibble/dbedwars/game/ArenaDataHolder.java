package org.zibble.dbedwars.game;

import org.bukkit.World;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableArena;
import org.zibble.dbedwars.utils.ConfigurationUtils;

import java.util.*;

public class ArenaDataHolder {

    private Message customName;
    private int maxPlayersPerTeam;
    private int minPlayersToStart;
    private boolean enabled;
    private World.Environment environment;

    private LocationXYZYP waitingLocation;
    private LocationXYZYP spectatorLocation;
    private LocationXYZ lobbyCorner1;
    private LocationXYZ lobbyCorner2;

    private final Map<Color, TeamDataHolder> teams;
    private final Set<SpawnerDataHolder> spawners;

    private ArenaDataHolder() {
        this.teams = new EnumMap<>(Color.class);
        this.spawners = new HashSet<>();
    }

    public static ArenaDataHolder create() {
        return new ArenaDataHolder();
    }

    public static ArenaDataHolder fromConfig(ConfigurableArena cfg) {
        ArenaDataHolder data = new ArenaDataHolder();
        data.customName = ConfigMessage.from(cfg.getCustomName());
        data.maxPlayersPerTeam = cfg.getPlayerInTeam();
        data.minPlayersToStart = cfg.getMinPlayers();
        data.enabled = cfg.isEnabled();
        data.environment = cfg.getEnvironment();
        data.waitingLocation = LocationXYZYP.valueOf(cfg.getLobbyLoc());
        data.spectatorLocation = LocationXYZYP.valueOf(cfg.getSpectatorLocation());
        data.lobbyCorner1 = LocationXYZ.valueOf(cfg.getLobbyPosMax());
        data.lobbyCorner2 = LocationXYZ.valueOf(cfg.getLobbyPosMin());
        for (ConfigurableArena.ConfigurableTeam value : cfg.getTeams().values()) {
            data.teams.put(value.getColor(), TeamDataHolder.fromConfig(value));
        }

        for (String spawner : cfg.getSpawners()) {
            data.spawners.add(SpawnerDataHolder.fromConfig(spawner));
        }
        return data;
    }

    public Message getCustomName() {
        return customName;
    }

    public void setCustomName(Message customName) {
        this.customName = customName;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public void setMaxPlayersPerTeam(int maxPlayersPerTeam) {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    public int getMinPlayersToStart() {
        return minPlayersToStart;
    }

    public void setMinPlayersToStart(int minPlayersToStart) {
        this.minPlayersToStart = minPlayersToStart;
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

    public void setEnvironment(World.Environment environment) {
        this.environment = environment;
    }

    public LocationXYZYP getWaitingLocation() {
        return waitingLocation;
    }

    public void setWaitingLocation(LocationXYZYP waitingLocation) {
        this.waitingLocation = waitingLocation;
    }

    public LocationXYZYP getSpectatorLocation() {
        return spectatorLocation;
    }

    public void setSpectatorLocation(LocationXYZYP spectatorLocation) {
        this.spectatorLocation = spectatorLocation;
    }

    public LocationXYZ getLobbyCorner1() {
        return lobbyCorner1;
    }

    public void setLobbyCorner1(LocationXYZ lobbyCorner1) {
        this.lobbyCorner1 = lobbyCorner1;
    }

    public LocationXYZ getLobbyCorner2() {
        return lobbyCorner2;
    }

    public void setLobbyCorner2(LocationXYZ lobbyCorner2) {
        this.lobbyCorner2 = lobbyCorner2;
    }

    public TeamDataHolder getTeamData(Color color) {
        return teams.get(color);
    }

    public Map<Color, TeamDataHolder> getTeamData() {
        return this.teams;
    }

    public Set<SpawnerDataHolder> getSpawners() {
        return spawners;
    }

    public static class TeamDataHolder {

        private final Color color;
        private LocationXYZYP spawnLocation;
        private LocationXYZYP shopNPC;
        private LocationXYZYP upgradesNPC;
        private LocationXYZ bed;

        private final Set<SpawnerDataHolder> spawners = new HashSet<>();

        public TeamDataHolder(Color color) {
            this.color = color;
        }

        public static TeamDataHolder fromConfig(ConfigurableArena.ConfigurableTeam cfg) {
            TeamDataHolder data = new TeamDataHolder(cfg.getColor());
            data.spawnLocation = LocationXYZYP.valueOf(cfg.getSpawn());
            data.shopNPC = LocationXYZYP.valueOf(cfg.getShopNpc());
            data.upgradesNPC = LocationXYZYP.valueOf(cfg.getUpgradesNpc());
            data.bed = LocationXYZ.valueOf(cfg.getBedLocation());

            for (String spawner : cfg.getSpawners()) {
                data.spawners.add(SpawnerDataHolder.fromConfig(spawner));
            }
            return data;
        }

        public Color getColor() {
            return color;
        }

        public LocationXYZYP getSpawnLocation() {
            return spawnLocation;
        }

        public void setSpawnLocation(LocationXYZYP spawnLocation) {
            this.spawnLocation = spawnLocation;
        }

        public LocationXYZYP getShopNPC() {
            return shopNPC;
        }

        public void setShopNPC(LocationXYZYP shopNPC) {
            this.shopNPC = shopNPC;
        }

        public LocationXYZYP getUpgradesNPC() {
            return upgradesNPC;
        }

        public void setUpgradesNPC(LocationXYZYP upgradesNPC) {
            this.upgradesNPC = upgradesNPC;
        }

        public LocationXYZ getBed() {
            return bed;
        }

        public void setBed(LocationXYZ bed) {
            this.bed = bed;
        }

        public Set<SpawnerDataHolder> getSpawners() {
            return spawners;
        }

    }

    public static class SpawnerDataHolder {

        private DropType dropType;
        private LocationXYZ location;

        public static SpawnerDataHolder fromConfig(String cfg) {
            SpawnerDataHolder data = new SpawnerDataHolder();
            Pair<DropType, LocationXYZ> pair = ConfigurationUtils.parseSpawner(cfg);
            data.dropType = pair.getKey();
            data.location = pair.getValue();
            return data;
        }

        public static SpawnerDataHolder of(DropType dropType, LocationXYZ location) {
            SpawnerDataHolder data = new SpawnerDataHolder();
            data.dropType = dropType;
            data.location = location;
            return data;
        }

        public DropType getDropType() {
            return dropType;
        }

        public void setDropType(DropType dropType) {
            this.dropType = dropType;
        }

        public LocationXYZ getLocation() {
            return location;
        }

        public void setLocation(LocationXYZ location) {
            this.location = location;
        }

    }
}
