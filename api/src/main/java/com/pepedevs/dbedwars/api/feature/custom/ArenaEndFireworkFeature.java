package com.pepedevs.dbedwars.api.feature.custom;

import com.pepedevs.dbedwars.api.feature.BedWarsFeature;
import com.pepedevs.dbedwars.api.feature.FeaturePriority;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.FireworkEffectAT;

public abstract class ArenaEndFireworkFeature extends BedWarsFeature {

    private FireworkEffectAT effect;

    public ArenaEndFireworkFeature() {
        super("ArenaEndFireworkFeature");
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
