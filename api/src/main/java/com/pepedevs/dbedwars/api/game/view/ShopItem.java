package com.pepedevs.dbedwars.api.game.view;

import com.pepedevs.dbedwars.api.game.view.attributes.AutoEquip;
import com.pepedevs.dbedwars.api.game.view.attributes.Colorable;
import com.pepedevs.dbedwars.api.game.view.attributes.Permanent;
import com.pepedevs.dbedwars.api.game.view.attributes.Purchasable;

public interface ShopItem extends Purchasable, Permanent, AutoEquip, Colorable {

    String getKey();
}
