package org.zibble.dbedwars.api.util.mixin;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.util.ClickType;

@FunctionalInterface
public interface ClickAction {

    void onClick(Player player, ClickType clickType);

}
