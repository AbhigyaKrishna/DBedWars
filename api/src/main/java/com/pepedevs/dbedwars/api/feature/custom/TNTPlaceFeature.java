package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.block.Block;

public abstract class TNTPlaceFeature extends BedWarsFeature {

    public TNTPlaceFeature() {
        super(BedWarsFeatures.TNT_PLACE_FEATURE.get());
    }

    public abstract void onPlace(Block block, ArenaPlayer placer);

}
