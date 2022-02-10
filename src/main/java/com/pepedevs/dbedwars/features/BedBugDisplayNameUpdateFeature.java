package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.nms.IBedBug;
import com.pepedevs.dbedwars.task.implementations.BedBugDisplayNameUpdateTask;
import com.pepedevs.dbedwars.api.task.Workload;
import org.bukkit.entity.Silverfish;

public class BedBugDisplayNameUpdateFeature extends com.pepedevs.dbedwars.api.feature.custom.BedBugDisplayNameUpdateFeature {

    private final DBedwars plugin;

    public BedBugDisplayNameUpdateFeature(DBedwars plugin) {
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
    public void start(Silverfish silverfish, ArenaPlayer thrower) {
        IBedBug bedBug = this.plugin.getNMSAdaptor().getAsBedwarsBedBug(silverfish, thrower.getTeam());
        Workload workload = new BedBugDisplayNameUpdateTask(
                bedBug.getSilverFish(),
                thrower.getTeam(),
                this.plugin.getConfigHandler().getCustomItems().getBedBug());
        this.plugin.getThreadHandler().submitAsync(workload);
    }

    @Override
    public boolean isInitialized() {
        return false;
    }
}
