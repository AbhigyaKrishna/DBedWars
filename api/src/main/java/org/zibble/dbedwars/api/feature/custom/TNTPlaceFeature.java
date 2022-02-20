package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.block.Block;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.ArenaPlayer;

public abstract class TNTPlaceFeature extends BedWarsFeature {

    public TNTPlaceFeature() {
        super(BedWarsFeatures.TNT_PLACE_FEATURE.get());
    }

    public abstract void onPlace(Block block, ArenaPlayer placer);

}
