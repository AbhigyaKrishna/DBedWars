package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.entity.IronGolem;

public abstract class DreamDefenderChaseFeature extends BedWarsFeature {

    public DreamDefenderChaseFeature() {
        super(BedWarsFeatures.DREAM_DEFENDER_CHASE_FEATURE.getKey());
    }

    public abstract void startChasing(IronGolem ironGolem, ArenaPlayer spawningPlayer);


}
