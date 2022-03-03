package org.zibble.dbedwars.api.game.setup;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.zibble.dbedwars.api.util.Color;

import java.util.List;
import java.util.Map;

public interface SetupSessionInfo {
    String getArenaCustomName();

    Location getWaitingLocation();

    Map<Color, ? extends TeamInfo> getTeamData();

    Block getWaitingLocationCorner1();

    Block getWaitingLocationCorner2();

    World.Environment getDimension();

    Location getSpectatorLocation();

    int getMaxPlayersPerTeam();

    int getMinPlayersToStart();

    interface TeamInfo {
        Location getSpawn();

        Location getBed();

        Location getShop();

        Location getUpgrades();

        List<Location> getGenLocations();
    }
}
