package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.entity.Silverfish;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.ArenaPlayer;

public abstract class BedBugChaseFeature extends BedWarsFeature {

    public BedBugChaseFeature() {
        super(BedWarsFeatures.BED_BUG_CHASE_FEATURE);
    }

    public abstract void startChase(Silverfish bedBug, ArenaPlayer throwingPlayer);

}
