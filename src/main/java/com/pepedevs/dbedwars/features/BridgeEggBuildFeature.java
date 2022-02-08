package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.task.BridgeEggWorkloadTask;
import com.pepedevs.radium.task.Workload;
import org.bukkit.entity.Egg;

public class BridgeEggBuildFeature extends com.pepedevs.dbedwars.api.feature.custom.BridgeEggBuildFeature {

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
