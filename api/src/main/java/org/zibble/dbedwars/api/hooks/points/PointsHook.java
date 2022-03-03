package org.zibble.dbedwars.api.hooks.points;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.Hook;

public interface PointsHook extends Hook {

    long getCurrentXP(@NotNull final Player player);

    void addXP(@NotNull final Player player, long toAdd);

    void reduceXP(@NotNull final Player player, long toReduce);
}
