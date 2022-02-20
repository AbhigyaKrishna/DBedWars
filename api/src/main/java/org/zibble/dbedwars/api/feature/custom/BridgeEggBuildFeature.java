package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.entity.Egg;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.ArenaPlayer;

public abstract class BridgeEggBuildFeature extends BedWarsFeature {

    public BridgeEggBuildFeature() {
        super(BedWarsFeatures.BRIDGE_EGG_BUILD_FEATURE.get());
    }

    public abstract void startBuilding(Egg egg, ArenaPlayer arenaPlayer);

}
