package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.nms.IBedBug;
import com.pepedevs.dbedwars.api.nms.NMSAdaptor;
import org.bukkit.entity.Silverfish;

public abstract class BedBugChaseFeature extends BedWarsFeature {

    public BedBugChaseFeature() {
        super(BedWarsFeatures.BED_BUG_CHASE_FEATURE.getKey());
    }

    public abstract void startChase(Silverfish bedBug, ArenaPlayer throwingPlayer);

}
