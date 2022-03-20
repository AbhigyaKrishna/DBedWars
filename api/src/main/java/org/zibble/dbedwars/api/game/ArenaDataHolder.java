package org.zibble.dbedwars.api.game;

import org.bukkit.World;
import org.zibble.dbedwars.api.game.spawner.DropType;
import org.zibble.dbedwars.api.game.view.ShopType;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZYP;
import org.zibble.dbedwars.api.util.Color;

import java.util.Map;
import java.util.Set;

public interface ArenaDataHolder {

    Message getCustomName();

    void setCustomName(Message customName);

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

    LocationXYZ getLobbyCorner1();

    void setLobbyCorner1(LocationXYZ lobbyCorner1);

    LocationXYZ getLobbyCorner2();

    void setLobbyCorner2(LocationXYZ lobbyCorner2);

    void addTeam(Color color);

    void removeTeam(Color color);

    TeamDataHolder getTeamData(Color color);

    Map<Color, ? extends TeamDataHolder> getTeamData();

    Set<? extends SpawnerDataHolder> getSpawners();

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

        DropType getDropType();

        void setDropType(DropType dropType);

        LocationXYZ getLocation();

        void setLocation(LocationXYZ location);

    }

    interface ShopDataHolder {

        ShopType getShopType();

        LocationXYZYP getLocation();

    }

}
