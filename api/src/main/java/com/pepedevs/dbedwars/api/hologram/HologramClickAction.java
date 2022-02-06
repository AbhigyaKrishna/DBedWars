package com.pepedevs.dbedwars.api.hologram;

import com.pepedevs.dbedwars.api.util.ClickType;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface HologramClickAction {

    void onClick(Player player, ClickType clickType);

}
