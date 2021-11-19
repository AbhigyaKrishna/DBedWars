package com.pepedevs.dbedwars.api.game.view.attributes;

import me.Abhigya.core.menu.inventory.action.ItemClickAction;

public interface PageChange {

    String getPage();

    void setPage(String page);

    void changePage(ItemClickAction action);

}
