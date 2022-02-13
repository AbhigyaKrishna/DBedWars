package com.pepedevs.dbedwars.api.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Hologram extends HologramObject {

    protected final String ID;

    protected Hologram(String ID, Location location) {
        super(location);
        this.ID = ID;
    }

    public abstract String getID();

    public abstract void show(int page, Player... players);

    public abstract HologramPage addPage();

    public abstract void changePage(int page, Player player);

    public abstract List<HologramPage> getPages();
}
