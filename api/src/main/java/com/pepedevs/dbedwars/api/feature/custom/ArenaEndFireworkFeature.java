package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.BedWarsFeatures;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;

public abstract class ArenaEndFireworkFeature extends BedWarsFeature {

    public ArenaEndFireworkFeature() {
        super(BedWarsFeatures.ARENA_END_FIREWORK_FEATURE.get());
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    public abstract void spawn(Team winner, Arena arena);

}
