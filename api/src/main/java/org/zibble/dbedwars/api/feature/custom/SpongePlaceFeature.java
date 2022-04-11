package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.block.Block;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.ArenaPlayer;

public abstract class SpongePlaceFeature extends BedWarsFeature {

    public SpongePlaceFeature() {
        super(BedWarsFeatures.SPONGE_PLACE_FEATURE);
    }

    public abstract void onPlace(Block sponge, ArenaPlayer placer);

}
