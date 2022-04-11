package org.zibble.dbedwars.api.feature.custom;

import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.util.Color;

public abstract class SaveArenaHistoryFeature extends BedWarsFeature {

    public SaveArenaHistoryFeature() {
        super(BedWarsFeatures.SAVE_ARENA_HISTORY_FEATURE);
    }

    public abstract boolean save(Arena arena, Color winners);

}
