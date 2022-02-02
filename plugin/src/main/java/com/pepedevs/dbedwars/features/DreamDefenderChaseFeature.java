package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.nms.IGolem;
import org.bukkit.entity.IronGolem;

public class DreamDefenderChaseFeature extends com.pepedevs.dbedwars.api.feature.custom.DreamDefenderChaseFeature {

    private final DBedwars plugin;

    public DreamDefenderChaseFeature(DBedwars plugin) {
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
    public void startChasing(IronGolem ironGolem, ArenaPlayer spawningPlayer) {
        IGolem golem = this.plugin.getNMSAdaptor().getBedwarsGolem(ironGolem, spawningPlayer.getTeam());
        golem.clearDefaultPathfinding().addCustomDefaults().initTargets(1);
    }

    @Override
    public boolean isInitialized() {
        return false;
    }
}
