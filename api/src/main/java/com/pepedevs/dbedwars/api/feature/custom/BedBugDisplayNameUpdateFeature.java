package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.entity.Silverfish;

public abstract class BedBugDisplayNameUpdateFeature extends BedWarsFeature {

    public BedBugDisplayNameUpdateFeature() {
        super(BedWarsFeatures.BED_BUG_DISPLAY_NAME_UPDATE_FEATURE.get());
    }

    public abstract void start(Silverfish bedBug, ArenaPlayer thrower);
}
