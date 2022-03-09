package org.zibble.dbedwars.api.hooks.hologram;

import org.bukkit.Location;

public interface HologramFactory {
    Hologram createHologram(Location location);
}
