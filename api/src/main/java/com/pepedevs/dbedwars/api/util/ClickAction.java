package com.pepedevs.dbedwars.api.util;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ClickAction {

    void onClick(Player player, ClickType clickType);

}
