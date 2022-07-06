package org.zibble.dbedwars.api.guis.component;

import org.bukkit.entity.Player;
import org.zibble.inventoryframework.menu.Menu;

public interface Gui<T extends Menu, R extends Gui> {

    R open(Player player);

    R close();

    R update();

    R updateSlot(int slot);

    Player getViewer();

}
