package org.zibble.dbedwars.api.hooks.vanish;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface VanishHook {

    boolean isVanished(@NotNull Player player);

    void vanish(@NotNull Player player);

    void unVanish(@NotNull Player player);
}
