package org.zibble.dbedwars.features;

import org.bukkit.Location;
import org.zibble.dbedwars.api.feature.FeaturePriority;

public class PreciseLocation extends org.zibble.dbedwars.api.feature.custom.PreciseLocation {

    @Override
    public FeaturePriority getPriority() {
        return FeaturePriority.NORMAL;
    }

    @Override
    public Location getPrecise(Location location) {
        Location precise = location.clone();
        location.setX(this.getNearestCoord(location.getX()));
        location.setY(this.getNearestCoord(location.getY()));
        location.setZ(this.getNearestCoord(location.getZ()));
        location.setYaw(this.getNearestYP(location.getYaw()));
        location.setPitch(this.getNearestYP(location.getPitch()));
        return precise;
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    private long getNearestCoord(double a) {
        return Math.round(a);
    }

    private long getNearestYP(double a) {
        double yp = a % 360;
        if (yp >= 22.5 && yp < 67.5) {
            return 45;
        }
        if (yp >= 67.5 && yp < 112.5) {
            return 90;
        }
        if (yp >= 112.5 && yp < 157.5) {
            return 135;
        }
        if (yp >= 157.5 && yp < 202.5) {
            return 180;
        }
        if (yp >= 202.5 && yp < 247.5) {
            return 225;
        }
        if (yp >= 247.5 && yp < 292.5) {
            return 270;
        }
        if (yp >= 292.5 && yp < 337.5) {
            return 315;
        }
        return 0;
    }

}
