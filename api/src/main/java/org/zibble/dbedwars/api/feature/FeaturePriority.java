package org.zibble.dbedwars.api.feature;

import java.util.Arrays;
import java.util.Comparator;

public enum FeaturePriority {

    LOW,
    NORMAL,
    HIGH,
    HIGHEST,
    ;

    public static final FeaturePriority[] ORDERED = values();

    static {
        Arrays.sort(ORDERED, new Comparator<FeaturePriority>() {
            @Override
            public int compare(FeaturePriority o1, FeaturePriority o2) {
                return -Integer.compare(o1.ordinal(), o2.ordinal());
            }
        });
    }

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
