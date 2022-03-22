package org.zibble.dbedwars.game;

import org.bukkit.World;
import org.zibble.dbedwars.api.game.ArenaDataHolder;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.game.view.ShopInfo;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.configuration.configurable.ConfigurableArena;
import org.zibble.dbedwars.utils.ConfigurationUtils;

import java.util.*;

public class ArenaDataHolderImpl implements ArenaDataHolder {

    private Message customName;
    private int maxPlayersPerTeam;
    private int minPlayersToStart;
    private boolean enabled;
    private World.Environment environment;

    private LocationXYZYP waitingLocation;
    private LocationXYZYP spectatorLocation;
    private LocationXYZ lobbyCorner1;
    private LocationXYZ lobbyCorner2;

    private final Map<Color, TeamDataHolderImpl> teams;
    private final Set<SpawnerDataHolder> spawners;

    private ArenaDataHolderImpl() {
        this.teams = new EnumMap<>(Color.class);
        this.spawners = new HashSet<>();
    }

    public static ArenaDataHolderImpl create() {
        return new ArenaDataHolderImpl();
    }

    @Override
    public Message getCustomName() {
        return customName;
    }

    @Override
    public void setCustomName(Message customName) {
        this.customName = customName;
    }

    @Override
    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    @Override
    public void setMaxPlayersPerTeam(int maxPlayersPerTeam) {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    @Override
    public int getMinPlayersToStart() {
        return minPlayersToStart;
    }

    @Override
    public void setMinPlayersToStart(int minPlayersToStart) {
        this.minPlayersToStart = minPlayersToStart;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public World.Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(World.Environment environment) {
        this.environment = environment;
    }

    @Override
    public LocationXYZYP getWaitingLocation() {
        return waitingLocation;
    }

    @Override
    public void setWaitingLocation(LocationXYZYP waitingLocation) {
        this.waitingLocation = waitingLocation;
    }

    @Override
    public LocationXYZYP getSpectatorLocation() {
        return spectatorLocation;
    }

    @Override
    public void setSpectatorLocation(LocationXYZYP spectatorLocation) {
        this.spectatorLocation = spectatorLocation;
    }

    @Override
    public LocationXYZ getLobbyCorner1() {
        return lobbyCorner1;
    }

    @Override
    public void setLobbyCorner1(LocationXYZ lobbyCorner1) {
        this.lobbyCorner1 = lobbyCorner1;
    }

    @Override
    public LocationXYZ getLobbyCorner2() {
        return lobbyCorner2;
    }

    @Override
    public void setLobbyCorner2(LocationXYZ lobbyCorner2) {
        this.lobbyCorner2 = lobbyCorner2;
    }

    @Override
    public void addTeam(Color color) {
        this.teams.put(color, new TeamDataHolderImpl(color));
    }

    @Override
    public void removeTeam(Color color) {
        this.teams.remove(color);
    }

    @Override
    public TeamDataHolderImpl getTeamData(Color color) {
        return teams.get(color);
    }

    @Override
    public Map<Color, TeamDataHolderImpl> getTeamData() {
        return this.teams;
    }

    @Override
    public Set<SpawnerDataHolder> getSpawners() {
        return spawners;
    }

    public static class TeamDataHolderImpl implements ArenaDataHolder.TeamDataHolder {

        private final Color color;
        private LocationXYZYP spawnLocation;
        private LocationXYZ bed;

        private final Set<ShopDataHolder> shops;
        private final Set<SpawnerDataHolderImpl> spawners;

        public TeamDataHolderImpl(Color color) {
            this.color = color;
            this.shops = new HashSet<>();
            this.spawners = new HashSet<>();
        }

        public static TeamDataHolderImpl fromConfig(ConfigurableArena.ConfigurableTeam cfg) {
            TeamDataHolderImpl data = new TeamDataHolderImpl(cfg.getColor());
            data.spawnLocation = LocationXYZYP.valueOf(cfg.getSpawn());
            data.bed = LocationXYZ.valueOf(cfg.getBedLocation());

            for (String spawner : cfg.getSpawners()) {
                data.spawners.add(SpawnerDataHolderImpl.fromConfig(spawner));
            }
            return data;
        }

        @Override
        public Color getColor() {
            return color;
        }

        @Override
        public LocationXYZYP getSpawnLocation() {
            return spawnLocation;
        }

        @Override
        public void setSpawnLocation(LocationXYZYP spawnLocation) {
            this.spawnLocation = spawnLocation;
        }

        @Override
        public Set<ShopDataHolder> getShops() {
            return shops;
        }

        @Override
        public LocationXYZ getBed() {
            return bed;
        }

        @Override
        public void setBed(LocationXYZ bed) {
            this.bed = bed;
        }

        @Override
        public Set<SpawnerDataHolderImpl> getSpawners() {
            return spawners;
        }

    }

    public static class SpawnerDataHolderImpl implements ArenaDataHolder.SpawnerDataHolder {

        private DropInfo dropType;
        private LocationXYZ location;

        public static SpawnerDataHolderImpl fromConfig(String cfg) {
            SpawnerDataHolderImpl data = new SpawnerDataHolderImpl();
            Pair<DropInfo, LocationXYZ> pair = ConfigurationUtils.parseSpawner(cfg);
            data.dropType = pair.getKey();
            data.location = pair.getValue();
            return data;
        }

        public static SpawnerDataHolderImpl of(DropInfo dropType, LocationXYZ location) {
            SpawnerDataHolderImpl data = new SpawnerDataHolderImpl();
            data.dropType = dropType;
            data.location = location;
            return data;
        }

        private SpawnerDataHolderImpl() {}

        @Override
        public DropInfo getDropType() {
            return dropType;
        }

        @Override
        public void setDropType(DropInfo dropType) {
            this.dropType = dropType;
        }

        @Override
        public LocationXYZ getLocation() {
            return location;
        }

        @Override
        public void setLocation(LocationXYZ location) {
            this.location = location;
        }

    }

    public static class ShopDataHolderImpl implements ArenaDataHolder.ShopDataHolder {

        private ShopInfo shopType;
        private LocationXYZYP location;

        public static ShopDataHolderImpl of(ShopInfo shopType, LocationXYZYP location) {
            ShopDataHolderImpl data = new ShopDataHolderImpl();
            data.shopType = shopType;
            data.location = location;
            return data;
        }

        private ShopDataHolderImpl() {}

        @Override
        public ShopInfo getShopType() {
            return shopType;
        }

        @Override
        public LocationXYZYP getLocation() {
            return location;
        }

    }


}
