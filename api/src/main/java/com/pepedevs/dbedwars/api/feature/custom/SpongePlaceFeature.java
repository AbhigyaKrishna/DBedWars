package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.block.Block;

public abstract class SpongePlaceFeature extends BedWarsFeature {

    public SpongePlaceFeature() {
        super(BedWarsFeatures.SPONGE_PLACE_FEATURE.getKey());
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    public abstract void onPlace(Block sponge, ArenaPlayer placer);
}
