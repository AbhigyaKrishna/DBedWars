package org.zibble.dbedwars.features;

import org.bukkit.entity.IronGolem;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.nms.IGolem;

public class DreamDefenderChaseFeature extends org.zibble.dbedwars.api.feature.custom.DreamDefenderChaseFeature {

    private final DBedwars plugin;

    public DreamDefenderChaseFeature(DBedwars plugin) {
        this.plugin = plugin;
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
        return true;
    }

}
