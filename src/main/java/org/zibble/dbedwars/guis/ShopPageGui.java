package org.zibble.dbedwars.guis;

import org.zibble.dbedwars.guis.component.GuiComponent;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

public class ShopPageGui extends GuiComponent<ChestMenu, ShopPageGui> {

    public ShopPageGui(int row) {
        super(new ChestMenu(row, null));
    }

}
