package com.pepedevs.dbedwars.api.game.view.attributes;

import com.pepedevs.dbedwars.api.game.view.ShopPage;
import me.Abhigya.core.menu.inventory.action.ItemClickAction;

public interface PageChange {

    ShopPage getPage();

    void setPage(ShopPage page);

    void changePage(ItemClickAction action);
}