package org.zibble.dbedwars.guis.setup;

import net.kyori.adventure.text.Component;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

public class SetupGui extends GuiComponent<ChestMenu, SetupGui> {

    protected SetupGui() {
        super(new ChestMenu(54, Component.text("Setup")));
    }

}
