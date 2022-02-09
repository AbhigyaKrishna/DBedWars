package com.pepedevs.dbedwars.api.hologram;

import org.bukkit.Location;

public abstract class HologramLine <C> extends HologramObject {

    protected HologramLine(Location location) {
        super(location);
    }

    public abstract HologramPage getParent();

    public abstract void setParent(HologramPage page);

    public abstract Type getType();

    protected abstract int[] getEntityIds();

    public abstract C getContent();

    public abstract void setContent(C content);

    public abstract double getOffsetY();

    /*public enum Type {
        UNKNOWN(0),
        TEXT(-0.5),
        HEAD(-2.0),
        SMALL_HEAD(-1.1875),
        ICON(-0.55),
        ENTITY(0);

        private final double offsetY;

        Type(double offsetY) {
            this.offsetY = offsetY;
        }

        public double getOffsetY() {
            return this.offsetY;
        }
    }*/
}
