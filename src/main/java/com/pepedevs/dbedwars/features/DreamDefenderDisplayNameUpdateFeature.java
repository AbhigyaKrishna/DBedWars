package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.task.implementations.GolemDisplayNameUpdateTask;
import com.pepedevs.dbedwars.api.task.Workload;
import org.bukkit.entity.IronGolem;

public class DreamDefenderDisplayNameUpdateFeature extends com.pepedevs.dbedwars.api.feature.custom.DreamDefenderDisplayNameUpdateFeature {

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
