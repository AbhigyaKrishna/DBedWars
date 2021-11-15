package com.pepedevs.dbedwars.api.util.item;

import com.pepedevs.dbedwars.api.util.NBTUtils;
import me.Abhigya.core.item.ActionItemBase;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public abstract class PluginActionItem extends ActionItemBase {

    public PluginActionItem(String display_name, Collection<String> lore, Material material) {
        super(display_name, lore, material);
    }

    @Override
    public ItemStack toItemStack() {
        return this.toBwItemStack().toItemStack();
    }

    public BwItemStack toBwItemStack() {
        return new BwItemStack(super.toItemStack());
    }

    @Override
    public boolean isThis(ItemStack item) {
        return super.isThis(item) && NBTUtils.hasPluginData(item);
    }

    @Override
    public void onActionPerform(
            Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {}
}
