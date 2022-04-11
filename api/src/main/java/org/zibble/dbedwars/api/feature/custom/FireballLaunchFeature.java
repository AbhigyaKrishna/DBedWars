package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.entity.Fireball;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.ArenaPlayer;

public abstract class FireballLaunchFeature extends BedWarsFeature {

    public FireballLaunchFeature() {
        super(BedWarsFeatures.FIREBALL_LAUNCH_FEATURE);
    }

    public abstract void launch(Fireball fireball, ArenaPlayer launcher);

}
