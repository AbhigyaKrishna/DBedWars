package org.zibble.dbedwars.api.game;

import org.bukkit.World;
import org.zibble.dbedwars.api.game.spawner.DropInfo;
import org.zibble.dbedwars.api.game.view.ShopInfo;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;

import java.util.Map;
import java.util.Set;

public interface ArenaDataHolder {

    String getId();

    Message getCustomName();

    void setCustomName(Message customName);

    ArenaCategory getCategory();

    void setCategory(ArenaCategory category);

    int getMaxPlayersPerTeam();

    void setMaxPlayersPerTeam(int maxPlayersPerTeam);

    int getMinPlayersToStart();

    void setMinPlayersToStart(int minPlayersToStart);

    World.Environment getEnvironment();

    void setEnvironment(World.Environment environment);

    LocationXYZYP getWaitingLocation();

    void setWaitingLocation(LocationXYZYP waitingLocation);

    LocationXYZYP getSpectatorLocation();

    void setSpectatorLocation(LocationXYZYP spectatorLocation);

    BoundingBox getLobbyArea();

    void setLobbyArea(BoundingBox lobbyArea);

    void addTeam(Color color);

    void removeTeam(Color color);

    TeamDataHolder getTeamData(Color color);

    Map<Color, ? extends TeamDataHolder> getTeamData();

    Set<? extends SpawnerDataHolder> getSpawners();

    int getMaxPlayers();

    interface TeamDataHolder {

        Color getColor();

        LocationXYZYP getSpawnLocation();

        void setSpawnLocation(LocationXYZYP spawnLocation);

        Set<? extends ShopDataHolder> getShops();

        LocationXYZ getBed();

        void setBed(LocationXYZ bed);

        Set<? extends SpawnerDataHolder> getSpawners();

    }

    interface SpawnerDataHolder {

        DropInfo getDropType();

        void setDropType(DropInfo dropType);

        LocationXYZ getLocation();

        void setLocation(LocationXYZ location);

    }

    interface ShopDataHolder {

        ShopInfo getShopType();

        LocationXYZYP getLocation();

    }

}
