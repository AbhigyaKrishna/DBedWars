package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.entity.IronGolem;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.ArenaPlayer;

public abstract class DreamDefenderChaseFeature extends BedWarsFeature {

    public DreamDefenderChaseFeature() {
        super(BedWarsFeatures.DREAM_DEFENDER_CHASE_FEATURE);
    }

    public abstract void startChasing(IronGolem ironGolem, ArenaPlayer spawningPlayer);


}
