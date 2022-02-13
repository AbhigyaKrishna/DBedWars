package com.pepedevs.dbedwars.api.hooks.hologram;

import com.pepedevs.dbedwars.api.util.ClickAction;
import com.pepedevs.dbedwars.api.util.Duration;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface Hologram {
    
    HologramPage addPage();

    List<HologramPage> getHologramPages();

    Map<UUID, Integer> getViewerPages();

    void changeViewerPage(UUID viewer, int page);

    int getCurrentPage(UUID viewer);

    int getDisplayRange();

    void setDisplayRange(int displayRange);

    int getUpdateRange();

    void setUpdateRange(int updateRange);

    Duration getUpdateInterval();

    void setUpdateInterval(Duration updateInterval);

    boolean isInverted();

    void setInverted(boolean inverted);

    Set<ClickAction> getClickActions();

    boolean isVisible(Player player);

    Location getLocation();

    void teleport(Location location);
}
