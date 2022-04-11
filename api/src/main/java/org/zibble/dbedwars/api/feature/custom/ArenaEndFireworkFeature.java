package org.zibble.dbedwars.api.feature.custom;

import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.feature.FeaturePriority;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.Team;

public abstract class ArenaEndFireworkFeature extends BedWarsFeature {

    public ArenaEndFireworkFeature() {
        super(BedWarsFeatures.ARENA_END_FIREWORK_FEATURE);
    }

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    public abstract void spawn(Team winner, Arena arena);

}
