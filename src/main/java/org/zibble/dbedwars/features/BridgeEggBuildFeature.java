package org.zibble.dbedwars.features;

import org.bukkit.entity.Egg;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.task.implementations.BridgeEggWorkloadTask;

public class BridgeEggBuildFeature extends org.zibble.dbedwars.api.feature.custom.BridgeEggBuildFeature {

    private final DBedwars plugin;

    public BridgeEggBuildFeature(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    @Override
    public void startBuilding(Egg egg, ArenaPlayer arenaPlayer) {
        Workload workload = new BridgeEggWorkloadTask(this.plugin, arenaPlayer, egg, this.plugin.getConfigHandler().getCustomItems().getBridgeEgg());
        this.plugin.getThreadHandler().submitAsync(workload);
    }

    @Override
    public boolean isInitialized() {
        return false;
    }
}
