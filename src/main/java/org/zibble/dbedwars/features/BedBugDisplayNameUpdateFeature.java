package org.zibble.dbedwars.features;

import org.bukkit.entity.Silverfish;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.nms.IBedBug;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.task.implementations.BedBugDisplayNameUpdateTask;

public class BedBugDisplayNameUpdateFeature extends org.zibble.dbedwars.api.feature.custom.BedBugDisplayNameUpdateFeature {

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
