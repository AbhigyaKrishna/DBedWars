package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.entity.IronGolem;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.ArenaPlayer;

public abstract class DreamDefenderDisplayNameUpdateFeature extends BedWarsFeature {

    public DreamDefenderDisplayNameUpdateFeature() {
        super(BedWarsFeatures.DREAM_DEFENDER_DISPLAY_NAME_UPDATE_FEATURE);
    }

    public abstract void start(IronGolem ironGolem, ArenaPlayer spawningPlayer);

}
