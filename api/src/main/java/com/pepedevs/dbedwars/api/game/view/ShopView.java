package com.pepedevs.dbedwars.api.game.view;

import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.util.Overridable;

import java.util.Map;

public interface ShopView extends Cloneable, Overridable {

    ArenaPlayer getPlayer();

    String getDefaultPage();

    Map<String, ViewItem> getCommons();

//    Map<String, ShopPage> getShopPages();
}
