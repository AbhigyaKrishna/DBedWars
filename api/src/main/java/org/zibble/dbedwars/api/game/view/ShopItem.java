package org.zibble.dbedwars.api.game.view;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.inventoryframework.ClickType;
import org.zibble.inventoryframework.MenuItem;
import org.zibble.inventoryframework.protocol.Item;

public interface ShopItem {

    ItemTierGroup getTierGroup();

    void setTierGroup(ItemTierGroup tierGroup);

    boolean canUse();

    void use(ArenaPlayer player, ClickType clickType);

    MenuItem<? extends Item> asMenuItem(ShopView shopView);

}
