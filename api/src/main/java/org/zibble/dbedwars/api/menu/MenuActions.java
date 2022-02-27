package org.zibble.dbedwars.api.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MenuActions {

    String tag();

    String description();

    boolean execute(@NotNull final Player player, @NotNull final String[] args);



}
