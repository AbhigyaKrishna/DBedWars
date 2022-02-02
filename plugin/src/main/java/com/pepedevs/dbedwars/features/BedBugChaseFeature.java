package com.pepedevs.dbedwars.features;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.nms.IBedBug;
import org.bukkit.entity.Silverfish;

public class BedBugChaseFeature extends com.pepedevs.dbedwars.api.feature.custom.BedBugChaseFeature {

    private final DBedwars plugin;

    public BedBugChaseFeature(DBedwars plugin) {
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
    public void startChase(Silverfish silverfish, ArenaPlayer throwingPlayer) {
        IBedBug bedBug = this.plugin.getNMSAdaptor().getAsBedwarsBedBug(silverfish, throwingPlayer.getTeam());
        bedBug.clearDefaultPathfinding()
                .addCustomDefaults()
                .initTargets(1);
    }

    @Override
    public boolean isInitialized() {
        return false;
    }
}
