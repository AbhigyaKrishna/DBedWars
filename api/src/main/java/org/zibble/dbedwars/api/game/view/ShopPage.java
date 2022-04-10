package org.zibble.dbedwars.api.game.view;

import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.key.Keyed;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

public interface ShopPage extends Keyed {

    int getRow();

    void setRow(int row);

    Message getTitle();

    void setTitle(Message title);

    String[] getMask();

    void setMask(String... mask);

    void addItem(char key, ShopItem item);

    GuiComponent<ChestMenu, ? extends GuiComponent> getGuiComponent();

}
