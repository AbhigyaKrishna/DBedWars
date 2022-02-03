package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.entity.Fireball;

public abstract class FireballLaunchFeature extends BedWarsFeature {

    public FireballLaunchFeature() {
        super(BedWarsFeatures.FIREBALL_LAUNCH_FEATURE.get());
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    public abstract void launch(Fireball fireball, ArenaPlayer launcher);

}
