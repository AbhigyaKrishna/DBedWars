package org.zibble.dbedwars.api.game.view;

public interface ItemTierGroup {

    void addItem(ShopItem item);

    ShopItem getItem(int tier);

    int getTierCount();

}
