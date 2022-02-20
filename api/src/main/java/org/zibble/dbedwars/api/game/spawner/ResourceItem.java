package org.zibble.dbedwars.api.game.spawner;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.api.util.BwItemStack;

public interface ResourceItem {

    BwItemStack getItem();

    void drop(Location location);

    void remove();

    @Nullable
    Item getItemEntity();

    boolean isDropped();

    boolean isMergeable();

    void setMergeable(boolean mergeable);

    boolean isSplittable();

    void setSplittable(boolean splittable);

}
