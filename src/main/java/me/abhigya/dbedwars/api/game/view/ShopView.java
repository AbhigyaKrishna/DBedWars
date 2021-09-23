package me.abhigya.dbedwars.api.game.view;

import me.abhigya.dbedwars.api.game.ArenaPlayer;
import me.abhigya.dbedwars.game.arena.view.ShopPage;
import me.abhigya.dbedwars.api.util.Overridable;

import java.util.Map;

public interface ShopView extends Cloneable, Overridable {

    ArenaPlayer getPlayer();

    String getDefaultPage();

    Map<String, ViewItem> getCommons();

    Map<String, ShopPage> getShopPages();

}
