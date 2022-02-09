package com.pepedevs.dbedwars.api.util.item;

import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.Keyed;
import com.pepedevs.dbedwars.api.util.NBTUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public abstract class BedWarsActionItem extends ActionItemBase implements Keyed<String> {

    public BedWarsActionItem(String display_name, Collection<String> lore, Material material) {
        super(display_name, lore, material);
    }

    public BedWarsActionItem(Component display_name, Collection<Component> lore, Material material) {
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
    public void onActionPerform(Player player, EnumAction enumAction, PlayerInteractEvent playerInteractEvent) {}

}
