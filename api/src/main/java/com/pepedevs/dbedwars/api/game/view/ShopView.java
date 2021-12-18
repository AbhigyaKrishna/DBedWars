package com.pepedevs.dbedwars.api.game.view;

import com.pepedevs.dbedwars.api.game.ArenaPlayer;

import java.util.Map;

public interface ShopView {

    ArenaPlayer getPlayer();

    ShopPage getDefaultPage();

    void setDefaultPage(ShopPage defaultPage);

    Map<String, com.pepedevs.dbedwars.api.game.view.ShopPage> getShopPages();

}
