package org.zibble.dbedwars.api.game.view;

import org.zibble.dbedwars.api.game.ArenaPlayer;

import java.util.Map;

public interface ShopView {

    ArenaPlayer getPlayer();

    ShopPage getDefaultPage();

    void setDefaultPage(ShopPage defaultPage);

    Map<String, ShopPage> getShopPages();
}
