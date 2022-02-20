package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.entity.Silverfish;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.ArenaPlayer;

public abstract class BedBugDisplayNameUpdateFeature extends BedWarsFeature {

    public BedBugDisplayNameUpdateFeature() {
        super(BedWarsFeatures.BED_BUG_DISPLAY_NAME_UPDATE_FEATURE.get());
    }

    public abstract void start(Silverfish bedBug, ArenaPlayer thrower);
}
