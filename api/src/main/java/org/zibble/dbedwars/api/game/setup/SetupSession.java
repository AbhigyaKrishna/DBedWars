package org.zibble.dbedwars.api.game.setup;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.zibble.dbedwars.api.util.Color;

public interface SetupSession {
    void init();

    void promptArenaCustomNameSet();

    void promptCleanupWorldEntity();

    void cleanupWorldEntities();

    void disableMobSpawning();

    void promptSetupWaitingLocation();

    void setupWaitingLocation(Location location);

    void setupLobbyCorner1(Block location);

    void setupLobbyCorner2(Block location);

    void promptSetupTeamsMessage();

    void startSetupTeam(Color color);

    void tryAutoSetupTeam(Color color);

    void setupTeamSpawn(Color color, Location location);

    void setupTeamShopNPC(Color color, Location location);

    void setupTeamUpgradesNPC(Color color, Location location);

    void setupBedLocation(Color color, Location location);

    void setupGenLocation(Color color, Location location);

    void cancel();

    void complete();

    boolean isPreciseEnabled();

    void setPreciseEnabled(boolean preciseEnabled);
}
