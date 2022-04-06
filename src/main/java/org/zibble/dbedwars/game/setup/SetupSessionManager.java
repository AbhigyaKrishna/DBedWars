package org.zibble.dbedwars.game.setup;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.game.ArenaDataHolderImpl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SetupSessionManager {

    private final Map<UUID, SetupSession> onGoingSessions;

    public SetupSessionManager() {
        this.onGoingSessions = new ConcurrentHashMap<>(0);
    }

    public SetupSession startSetupSession(World world, Player player, ArenaDataHolderImpl dataHolder) {
        SetupSession setupSession = new SetupSession(world, player, dataHolder);
        this.onGoingSessions.put(player.getUniqueId(), setupSession);
        return setupSession;
    }

    public boolean isInSetupSession(Player player) {
        return this.onGoingSessions.containsKey(player.getUniqueId());
    }

    public SetupSession getSetupSessionOf(Player player) {
        return this.onGoingSessions.get(player.getUniqueId());
    }

}
