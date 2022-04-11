package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.ArenaPlayer;

import java.util.Collection;

public abstract class DeathAnimationFeature extends BedWarsFeature {

    public DeathAnimationFeature() {
        super(BedWarsFeatures.DEATH_ANIMATION_FEATURE);
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    public abstract void play(ArenaPlayer player, Collection<Player> viewers);

}
