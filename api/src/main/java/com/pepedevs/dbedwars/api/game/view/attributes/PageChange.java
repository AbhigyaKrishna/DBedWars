package com.pepedevs.dbedwars.api.game.view.attributes;

import com.pepedevs.radium.gui.inventory.action.ItemClickAction;
import com.pepedevs.dbedwars.api.game.view.ShopPage;

public interface PageChange {

    ShopPage getPage();

    void setPage(ShopPage page);

    void changePage(ItemClickAction action);
}
