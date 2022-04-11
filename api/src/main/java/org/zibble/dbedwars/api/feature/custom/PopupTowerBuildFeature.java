package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.block.Chest;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.ArenaPlayer;

public abstract class PopupTowerBuildFeature extends BedWarsFeature {


    public PopupTowerBuildFeature() {
        super(BedWarsFeatures.POPUP_TOWER_BUILD_FEATURE);
    }

    public abstract void build(Chest chest, ArenaPlayer placer);

}
