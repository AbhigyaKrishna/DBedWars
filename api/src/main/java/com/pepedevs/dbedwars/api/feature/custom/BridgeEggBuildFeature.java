package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.entity.Egg;

public abstract class BridgeEggBuildFeature extends BedWarsFeature {

    public BridgeEggBuildFeature() {
        super(BedWarsFeatures.BRIDGE_EGG_BUILD_FEATURE.getKey());
    }

    public abstract void startBuilding(Egg egg, ArenaPlayer arenaPlayer);

}
