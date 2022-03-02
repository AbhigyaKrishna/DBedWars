package org.zibble.dbedwars.api.feature.custom;

import org.bukkit.Location;
import org.zibble.dbedwars.api.feature.BedWarsFeature;
import org.zibble.dbedwars.api.feature.BedWarsFeatures;

public abstract class PreciseLocation extends BedWarsFeature {

    public PreciseLocation() {
        super(BedWarsFeatures.PRECISE_LOCATION.get());
    }

    @Override
    public boolean isTickable() {
        return false;
    }

    public abstract Location getPrecise(Location location);
}
