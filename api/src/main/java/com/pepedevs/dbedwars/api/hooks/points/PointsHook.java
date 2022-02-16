package com.pepedevs.dbedwars.api.hooks.points;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PointsHook {

    long getCurrentXP(@NotNull final Player player);

    void addXP(@NotNull final Player player, long toAdd);

    void reduceXP(@NotNull final Player player, long toReduce);
}
