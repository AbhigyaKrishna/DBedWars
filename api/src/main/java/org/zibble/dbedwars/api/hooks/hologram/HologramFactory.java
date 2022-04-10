package org.zibble.dbedwars.api.hooks.hologram;

import org.bukkit.Location;
import org.zibble.dbedwars.api.hooks.Hook;

public interface HologramFactory extends Hook {

    Hologram createHologram(Location location);

}
