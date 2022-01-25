package com.pepedevs.dbedwars.api.feature;

public enum FeaturePriority {

    LOW,
    NOMRAL,
    HIGH,
    HIGHEST,
    ;

    public boolean isHigherThan(FeaturePriority priority) {
        return this.ordinal() > priority.ordinal();
    }

    public boolean isLowerThan(FeaturePriority priority) {
        return this.ordinal() < priority.ordinal();
    }

}
