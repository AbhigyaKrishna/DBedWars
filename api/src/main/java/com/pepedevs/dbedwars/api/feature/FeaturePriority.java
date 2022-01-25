package com.pepedevs.dbedwars.api.feature;

public enum FeaturePriority {

    LOW,
    NORMAL,
    HIGH,
    HIGHEST,
    ;

    public boolean isHigherThan(FeaturePriority priority) {
        return this.ordinal() > priority.ordinal();
    }

    public boolean isLowerThan(FeaturePriority priority) {
        return this.ordinal() < priority.ordinal();
    }

    public FeaturePriority getHigherPriority() {
        return (this == HIGHEST ? HIGHEST : values()[this.ordinal() + 1]);
    }

    public FeaturePriority getLowerPriority() {
        return (this == LOW ? LOW : values()[this.ordinal() - 1]);
    }

}
