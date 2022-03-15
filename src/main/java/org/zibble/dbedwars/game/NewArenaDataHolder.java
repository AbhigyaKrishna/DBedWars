package org.zibble.dbedwars.game;

import org.bukkit.World;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.configuration.configurable.ConfigurableArena;
import org.zibble.dbedwars.utils.ConfigurationUtils;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NewArenaDataHolder {

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

    private NewArenaDataHolder() {
        this.teams = new EnumMap<>(Color.class);
        this.spawners = new HashSet<>();
    }

    public static NewArenaDataHolder create() {
        return new NewArenaDataHolder();
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

    public void addTeam(Color color) {
        this.teams.put(color, new TeamDataHolder(color));
    }

    public void removeTeam(Color color) {
        this.teams.remove(color);
    }

    public NewArenaDataHolder.TeamDataHolder getTeamData(Color color) {
        return teams.get(color);
    }

    public Map<Color, NewArenaDataHolder.TeamDataHolder> getTeamData() {
        return this.teams;
    }

    public Set<NewArenaDataHolder.SpawnerDataHolder> getSpawners() {
        return spawners;
    }

    public static class TeamDataHolder {

        private final Color color;
        private LocationXYZYP spawnLocation;
        private LocationXYZ bed;

        private final Set<ShopDataHolder> shops;
        private final Set<NewArenaDataHolder.SpawnerDataHolder> spawners;

        public TeamDataHolder(Color color) {
            this.color = color;
            this.shops = new HashSet<>();
            this.spawners = new HashSet<>();
        }

        public static NewArenaDataHolder.TeamDataHolder fromConfig(ConfigurableArena.ConfigurableTeam cfg) {
            NewArenaDataHolder.TeamDataHolder data = new NewArenaDataHolder.TeamDataHolder(cfg.getColor());
            data.spawnLocation = LocationXYZYP.valueOf(cfg.getSpawn());
            data.shopNPC = LocationXYZYP.valueOf(cfg.getShopNpc());
            data.upgradesNPC = LocationXYZYP.valueOf(cfg.getUpgradesNpc());
            data.bed = LocationXYZ.valueOf(cfg.getBedLocation());

            for (String spawner : cfg.getSpawners()) {
                data.spawners.add(NewArenaDataHolder.SpawnerDataHolder.fromConfig(spawner));
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

        public Set<ShopDataHolder> getShops() {
            return shops;
        }

        public LocationXYZ getBed() {
            return bed;
        }

        public void setBed(LocationXYZ bed) {
            this.bed = bed;
        }

        public Set<NewArenaDataHolder.SpawnerDataHolder> getSpawners() {
            return spawners;
        }

    }

    public static class SpawnerDataHolder {

        private DropType dropType;
        private LocationXYZ location;

        public static NewArenaDataHolder.SpawnerDataHolder fromConfig(String cfg) {
            NewArenaDataHolder.SpawnerDataHolder data = new NewArenaDataHolder.SpawnerDataHolder();
            Pair<DropType, LocationXYZ> pair = ConfigurationUtils.parseSpawner(cfg);
            data.dropType = pair.getKey();
            data.location = pair.getValue();
            return data;
        }

        public static NewArenaDataHolder.SpawnerDataHolder of(DropType dropType, LocationXYZ location) {
            NewArenaDataHolder.SpawnerDataHolder data = new NewArenaDataHolder.SpawnerDataHolder();
            data.dropType = dropType;
            data.location = location;
            return data;
        }

        private SpawnerDataHolder() {}

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

    public static class ShopDataHolder {

        private ShopType shopType;
        private LocationXYZYP location;

        public static NewArenaDataHolder.ShopDataHolder of(ShopType shopType, LocationXYZYP location) {
            NewArenaDataHolder.ShopDataHolder data = new NewArenaDataHolder.ShopDataHolder();
            data.shopType = shopType;
            data.location = location;
            return data;
        }

        private ShopDataHolder() {}

        public ShopType getShopType() {
            return shopType;
        }

        public LocationXYZYP getLocation() {
            return location;
        }

    }


}
