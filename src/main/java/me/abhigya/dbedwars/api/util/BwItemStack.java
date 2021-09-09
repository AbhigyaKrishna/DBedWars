package me.abhigya.dbedwars.api.util;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.Abhigya.core.util.itemstack.ItemMetaBuilder;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BwItemStack implements Cloneable {

    private ItemStack item;

    public BwItemStack(Material material) {
        this.item = new ItemStack(material);
    }

    public BwItemStack(Material material, int amount) {
        this.item = new ItemStack(material, amount);
    }

    public BwItemStack(ItemStack stack) {
        this.item = stack.clone();
    }

    public ItemMetaBuilder getItemMetaBuilder() {
        return new ItemMetaBuilder(this.item.getType());
    }

    public void setItemMetaBuilder(ItemMetaBuilder builder) {
        builder.applyTo(this.item);
    }

    public BwItemStack setUnMergeable() {
        NBTItem nbti = new NBTItem(this.item);
        nbti.setBoolean("unmerge", true);
        this.item = nbti.getItem();
        return this;
    }

    public ItemStack toItemStack() {
        if (!Utils.hasPluginData(this.item))
            this.item = Utils.addPluginData(this.item);
        return this.item;
    }

    public ItemMeta getItemMeta() {
        return this.item.getItemMeta();
    }

    public void setItemMeta(ItemMeta meta) {
        this.item.setItemMeta(meta);
    }

    public Material getType() {
        return this.item.getType();
    }

    public void setType(Material type) {
        this.item.setType(type);
    }

    public int getAmount() {
        return this.item.getAmount();
    }

    public void setAmount(int amount) {
        this.item.setAmount(amount);
    }

    public short getData() {
        return this.item.getDurability();
    }

    public void setData(short data) {
        this.item.setDurability(data);
    }

    @Override
    public BwItemStack clone() {
        try {
            return (BwItemStack) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
