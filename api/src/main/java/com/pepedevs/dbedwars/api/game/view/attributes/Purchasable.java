package com.pepedevs.dbedwars.api.game.view.attributes;

import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public interface Purchasable {

    Set<ItemStack> getCost();

    BwItemStack getItem();

    boolean isCostFullFilled(Player player);

    void onPurchase(ArenaPlayer player);
}
