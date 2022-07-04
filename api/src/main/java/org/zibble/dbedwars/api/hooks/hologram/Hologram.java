package org.zibble.dbedwars.api.hooks.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.util.key.Keyed;
import org.zibble.dbedwars.api.util.mixin.ClickAction;
import org.zibble.dbedwars.api.objects.serializable.Duration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface Hologram extends Keyed {

    HologramPage addPage();

    List<HologramPage> getHologramPages();

    Map<UUID, Integer> getViewerPages();

    void changeViewerPage(Player viewer, int page);

    HologramPage getCurrentPage(Player viewer);

    int getDisplayRange();

    void setDisplayRange(int displayRange);

    int getUpdateRange();

    void setUpdateRange(int updateRange);

    Duration getUpdateInterval();

    void setUpdateInterval(Duration updateInterval);

    boolean isInverted();

    void setInverted(boolean inverted);

    Set<ClickAction> getClickActions();

    void addClickAction(ClickAction action);

    boolean isVisible(Player player);

    boolean isClickRegistered();

    void setClickRegistered(boolean clickRegistered);

    boolean isUpdateRegistered();

    void setUpdateRegistered(boolean updateRegistered);

    void show(Player... players);

    void updateContent(Player... players);

    void updateLocation(Player... players);

    void hide(Player... players);

    void hideAll();

    Location getLocation();

    void teleport(Location location);

    long getLastUpdate();

    void destroy();

}
