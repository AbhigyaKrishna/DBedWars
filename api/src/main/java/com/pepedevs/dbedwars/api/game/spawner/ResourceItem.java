package com.pepedevs.dbedwars.api.game.spawner;

import com.pepedevs.dbedwars.api.util.BwItemStack;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.Nullable;

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
