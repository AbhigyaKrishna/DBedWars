package org.zibble.dbedwars.api.game.view;

import org.zibble.dbedwars.api.game.view.attributes.AutoEquip;
import org.zibble.dbedwars.api.game.view.attributes.Colorable;
import org.zibble.dbedwars.api.game.view.attributes.Permanent;
import org.zibble.dbedwars.api.game.view.attributes.Purchasable;

public interface ShopItem extends Purchasable, Permanent, AutoEquip, Colorable {

    String getKey();
}
