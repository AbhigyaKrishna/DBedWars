package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.entity.IronGolem;

public abstract class DreamDefenderDisplayNameUpdateFeature extends BedWarsFeature {

    public DreamDefenderDisplayNameUpdateFeature() {
        super(BedWarsFeatures.DREAM_DEFENDER_DISPLAY_NAME_UPDATE_FEATURE.get());
    }

    public abstract void start(IronGolem ironGolem, ArenaPlayer spawningPlayer);

}
