package com.pepedevs.dbedwars.api.hologram;

import org.bukkit.Location;

public abstract class HologramLine <C> extends HologramObject {

    protected HologramLine(Location location) {
        super(location);
    }

    public abstract HologramPage getParent();

    public abstract void setParent(HologramPage page);

    protected abstract int[] getEntityIds();

    public abstract C getContent();

    public abstract void setContent(C content);
}
