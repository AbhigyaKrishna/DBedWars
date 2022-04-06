package org.zibble.dbedwars.api.hooks.selection;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.api.hooks.Hook;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;

public interface AreaSelectionHook extends Hook {

    LocationXYZ getFirstLocation(Player player);

    void setFirstLocation(Player player, LocationXYZ location);

    LocationXYZ getSecondLocation(Player player);

    void setSecondLocation(Player player, LocationXYZ location);

    @Nullable BoundingBox getSelection(Player player);

    void clearSelection(Player player);

}
