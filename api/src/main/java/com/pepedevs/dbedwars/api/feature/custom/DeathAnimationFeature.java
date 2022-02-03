package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import org.bukkit.entity.Player;

import java.util.Collection;

public abstract class DeathAnimationFeature extends BedWarsFeature {

    public DeathAnimationFeature() {
        super(BedWarsFeatures.DEATH_ANIMATION_FEATURE.get());
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    public abstract void play(Player player, Collection<Player> viewers);

}
