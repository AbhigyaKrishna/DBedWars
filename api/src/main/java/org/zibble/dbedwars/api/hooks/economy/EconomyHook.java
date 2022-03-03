package org.zibble.dbedwars.api.hooks.economy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.hooks.Hook;

public interface EconomyHook extends Hook {

    double getBalance(@NotNull final Player player);

    void addBalance(@NotNull final Player player, double balanceToAdd);

    void removeBalance(@NotNull final Player player, double balanceToRemove);

    String getBalanceFormatted(@NotNull final Player player);

}
