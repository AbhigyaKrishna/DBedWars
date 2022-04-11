package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.Location;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;

public abstract class PreciseLocationFeature extends BedWarsFeature {

    public PreciseLocationFeature() {
        super(BedWarsFeatures.PRECISE_LOCATION_FEATURE);
    }

    public abstract Location getPrecise(Location location);

}
