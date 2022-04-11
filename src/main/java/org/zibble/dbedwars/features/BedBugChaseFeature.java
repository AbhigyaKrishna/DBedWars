package org.zibble.dbedwars.features;

import org.bukkit.entity.Silverfish;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.nms.IBedBug;

public class BedBugChaseFeature extends org.zibble.dbedwars.api.feature.custom.BedBugChaseFeature {

    private final DBedwars plugin;

    public BedBugChaseFeature(DBedwars plugin) {
        this.plugin = plugin;
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
        return true;
    }

}
