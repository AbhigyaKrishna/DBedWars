package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.entity.Silverfish;

public abstract class BedBugChaseFeature extends BedWarsFeature {

    public BedBugChaseFeature() {
        super(BedWarsFeatures.BED_BUG_CHASE_FEATURE.get());
    }

    public abstract void startChase(Silverfish bedBug, ArenaPlayer throwingPlayer);

}
