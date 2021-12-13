package com.pepedevs.dbedwars.api.game.view.attributes;

import com.pepedevs.dbedwars.api.game.view.ShopPage;
import com.pepedevs.corelib.gui.inventory.action.ItemClickAction;

public interface PageChange {

    ShopPage getPage();

    void setPage(ShopPage page);

    void changePage(ItemClickAction action);
}
