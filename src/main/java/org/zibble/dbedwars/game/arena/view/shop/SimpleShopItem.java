package org.zibble.dbedwars.game.arena.view.shop;

import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.inventoryframework.ClickType;

public class SimpleShopItem extends ShopItem {

    public SimpleShopItem(ItemStack item) {
        super(item);
    }

    @Override
    public void use(ArenaPlayer player, ClickType clickType) {
        player.getPlayer().getInventory().addItem(this.item);
    }

}
