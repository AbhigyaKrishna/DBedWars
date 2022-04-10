package org.zibble.dbedwars.features;

import org.bukkit.entity.IronGolem;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.task.implementations.GolemDisplayNameUpdateTask;

public class DreamDefenderDisplayNameUpdateFeature extends org.zibble.dbedwars.api.feature.custom.DreamDefenderDisplayNameUpdateFeature {

    private final DBedwars plugin;

    public DreamDefenderDisplayNameUpdateFeature(DBedwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    @Override
    public void start(IronGolem ironGolem, ArenaPlayer spawningPlayer) {
        Workload workload = new GolemDisplayNameUpdateTask(ironGolem, spawningPlayer.getTeam(), this.plugin.getConfigHandler().getCustomItems().getDreamDefender());
        this.plugin.getThreadHandler().submitAsync(workload);
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

}
