package org.zibble.dbedwars.game.setup;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SetupSessionManager {

    private final Map<UUID, SetupSessionImpl> onGoingSessions;
    private final DBedwars plugin;

    public SetupSessionManager(DBedwars plugin) {
        this.plugin = plugin;
        this.onGoingSessions = new ConcurrentHashMap<>(0);
    }

    public void startSetupSession(World world, Player player) {
        SetupSessionImpl setupSession = new SetupSessionImpl(world, player);
        setupSession.init();
        this.onGoingSessions.put(player.getUniqueId(), setupSession);
    }

    public boolean isInSetupSession(Player player) {
        return this.onGoingSessions.containsKey(player.getUniqueId());
    }

    public SetupSessionImpl getSetupSessionOf(Player player) {
        return this.onGoingSessions.get(player.getUniqueId());
    }

}
