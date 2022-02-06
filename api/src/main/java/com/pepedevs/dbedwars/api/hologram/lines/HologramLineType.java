package com.pepedevs.dbedwars.api.hologram.lines;

public enum HologramLineType {
    UNKNOWN(0),
    TEXT(-0.5),
    HEAD(-2.0),
    SMALL_HEAD(-1.1875),
    ICON(-0.55),
    ENTITY(0);

    private final double offsetY;

    HologramLineType(double offsetY) {
        this.offsetY = offsetY;
    }

    public double getOffsetY() {
        return this.offsetY;
    }
}

