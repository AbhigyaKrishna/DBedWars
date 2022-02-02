package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.block.Chest;

public abstract class PopupTowerBuildFeature extends BedWarsFeature {


    public PopupTowerBuildFeature() {
        super(BedWarsFeatures.POPUP_TOWER_BUILD_FEATURE.getKey());
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    public abstract void build(Chest chest, ArenaPlayer placer);

}
