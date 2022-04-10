package org.zibble.dbedwars.api.game.view;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.messaging.message.Message;

public interface ShopView {

    ArenaPlayer getPlayer();

    ShopPage getDefaultPage();

    void setDefaultPage(ShopPage page);

    ShopPage createPage(String key, int row, Message title);

    void addPage(String key, ShopPage page);

}
