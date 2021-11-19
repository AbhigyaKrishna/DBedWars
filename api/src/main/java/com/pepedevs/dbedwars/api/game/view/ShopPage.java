package com.pepedevs.dbedwars.api.game.view;

import java.util.Map;

public interface ShopPage {

    String getKey();

    ShopView getView();

    void setView(ShopView view);

    String getTitle();

    void setTitle(String title);

    GuiItem[][] getPattern();

    Map<String, GuiItem> getItems();
}