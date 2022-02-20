package org.zibble.dbedwars.api.game.view.attributes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.util.BwItemStack;

import java.util.Set;

public interface Purchasable {

    Set<ItemStack> getCost();

    BwItemStack getItem();

    boolean isCostFullFilled(Player player);

    void onPurchase(ArenaPlayer player);
}
