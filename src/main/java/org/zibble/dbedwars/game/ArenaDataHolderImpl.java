package org.zibble.dbedwars.game;

import org.bukkit.World;
import org.zibble.dbedwars.api.game.ArenaCategory;
import org.zibble.dbedwars.api.game.ArenaDataHolder;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.Pair;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableArena;
import org.zibble.dbedwars.game.arena.ArenaCategoryImpl;
import org.zibble.dbedwars.game.arena.view.ShopInfoImpl;
import org.zibble.dbedwars.utils.ConfigurationUtil;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ArenaDataHolderImpl implements ArenaDataHolder {

    private final String id;
    private final Map<Color, TeamDataHolderImpl> teams;
    private final Set<SpawnerDataHolderImpl> spawners;
    private String worldFileName;
    private Message customName;
    private ArenaCategoryImpl category;
    private int maxPlayersPerTeam;
    private int minPlayersToStart;
    private boolean enabled;
    private World.Environment environment;
    private LocationXYZYP waitingLocation;
    private LocationXYZYP spectatorLocation;
    private BoundingBox lobbyArea;

    private ArenaDataHolderImpl(String id) {
        this.id = id;
        this.teams = new EnumMap<>(Color.class);
        this.spawners = new HashSet<>();
    }

    public static ArenaDataHolderImpl fromConfig(GameManagerImpl gameManager, ConfigurableArena config) {
        ArenaDataHolderImpl holder = new ArenaDataHolderImpl(config.getIdentifier());
        holder.setCustomName(config.getCustomName() != null ? ConfigMessage.from(config.getCustomName()) : null);
        holder.setCategory(config.getCategory() != null ? gameManager.getCategory(config.getCategory()) : null);
        holder.setMaxPlayersPerTeam(config.getPlayerInTeam());
        holder.setMinPlayersToStart(config.getMinPlayers());
        holder.setEnabled(config.isEnabled());
        holder.setEnvironment(config.getEnvironment());
        holder.setWaitingLocation(config.getLobbyLocation() != null ? LocationXYZYP.valueOf(config.getLobbyLocation()) : null);
        holder.setSpectatorLocation(config.getSpectatorLocation() != null ? LocationXYZYP.valueOf(config.getSpectatorLocation()) : null);
        holder.setLobbyArea(config.getLobbyPosMax() != null && config.getLobbyPosMin() != null ? new BoundingBox(LocationXYZ.valueOf(config.getLobbyPosMax()).toVector(), LocationXYZ.valueOf(config.getLobbyPosMin()).toVector()) : null);

        if (config.getTeams() != null) {
            for (Map.Entry<String, ConfigurableArena.ConfigurableTeam> entry : config.getTeams().entrySet()) {
                TeamDataHolderImpl team = TeamDataHolderImpl.fromConfig(entry.getValue());
                holder.getTeamData().put(team.getColor(), team);
            }
        }

        if (config.getSpawners() != null) {
            for (String spawner : config.getSpawners()) {
                holder.getSpawners().add(SpawnerDataHolderImpl.fromConfig(spawner));
            }
        }

        return holder;
    }

    public static ArenaDataHolderImpl create(String id) {
        return new ArenaDataHolderImpl(id);
    }

    @Override
    public String getId() {
        return id;
    }

    public String getWorldFileName() {
        return this.worldFileName;
    }

    public void setWorldFileName(String worldFileName) {
        this.worldFileName = worldFileName;
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
    public ArenaCategoryImpl getCategory() {
        return category;
    }

    @Override
    public void setCategory(ArenaCategory category) {
        this.category = (ArenaCategoryImpl) category;
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
    public BoundingBox getLobbyArea() {
        return lobbyArea;
    }

    @Override
    public void setLobbyArea(BoundingBox lobbyArea) {
        this.lobbyArea = lobbyArea;
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
    public Set<SpawnerDataHolderImpl> getSpawners() {
        return spawners;
    }

    @Override
    public int getMaxPlayers() {
        return this.maxPlayersPerTeam * this.teams.size();
    }

    public static class TeamDataHolderImpl implements ArenaDataHolder.TeamDataHolder {

        private final Color color;
        private final Set<ShopDataHolderImpl> shops;
        private final Set<SpawnerDataHolderImpl> spawners;
        private LocationXYZYP spawnLocation;
        private LocationXYZ bed;

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
        public Set<ShopDataHolderImpl> getShops() {
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

        @Override
        public String toString() {
            return "TeamDataHolderImpl{" +
                    "color=" + color +
                    ", shops=" + shops +
                    ", spawners=" + spawners +
                    ", spawnLocation=" + spawnLocation +
                    ", bed=" + bed +
                    '}';
        }

    }

    public static class SpawnerDataHolderImpl implements ArenaDataHolder.SpawnerDataHolder {

        private DropInfo dropType;
        private LocationXYZ location;

        private SpawnerDataHolderImpl() {
        }

        public static SpawnerDataHolderImpl fromConfig(String cfg) {
            SpawnerDataHolderImpl data = new SpawnerDataHolderImpl();
            Pair<DropInfo, LocationXYZ> pair = ConfigurationUtil.parseSpawner(cfg);
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

        @Override
        public String toString() {
            return "SpawnerDataHolderImpl{" +
                    "dropType=" + dropType +
                    ", location=" + location +
                    '}';
        }

    }

    public static class ShopDataHolderImpl implements ArenaDataHolder.ShopDataHolder {

        private ShopInfoImpl shopType;
        private LocationXYZYP location;

        private ShopDataHolderImpl() {
        }

        public static ShopDataHolderImpl of(ShopInfoImpl shopType, LocationXYZYP location) {
            ShopDataHolderImpl data = new ShopDataHolderImpl();
            data.shopType = shopType;
            data.location = location;
            return data;
        }

        @Override
        public ShopInfoImpl getShopType() {
            return shopType;
        }

        @Override
        public LocationXYZYP getLocation() {
            return location;
        }

        @Override
        public String toString() {
            return "ShopDataHolderImpl{" +
                    "shopType=" + shopType +
                    ", location=" + location +
                    '}';
        }

    }

    @Override
    public String toString() {
        return "ArenaDataHolderImpl{" +
                "id='" + id + '\'' +
                ", teams=" + teams +
                ", spawners=" + spawners +
                ", worldFileName='" + worldFileName + '\'' +
                ", customName=" + customName +
                ", category=" + category +
                ", maxPlayersPerTeam=" + maxPlayersPerTeam +
                ", minPlayersToStart=" + minPlayersToStart +
                ", enabled=" + enabled +
                ", environment=" + environment +
                ", waitingLocation=" + waitingLocation +
                ", spectatorLocation=" + spectatorLocation +
                ", lobbyArea=" + lobbyArea +
                '}';
    }

}
