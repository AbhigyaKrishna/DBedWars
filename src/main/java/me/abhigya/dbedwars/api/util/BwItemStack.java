package me.abhigya.dbedwars.api.util;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.Abhigya.core.util.itemstack.ItemMetaBuilder;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

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

    public void applyEnchant(LEnchant enchant) {
        enchant.applyUnsafe(this.item);
    }

    public void addNBT(String key, Object value) {
        this.item = Utils.addNbtData(this.item, key, value);
    }

    public boolean hasNBT(String key) {
        return Utils.hasNBTData(this.item, key);
    }

    public <T> T getValue(String key, Class<T> clazz) {
        return Utils.getValue(this.item, key, clazz);
    }

    public void removeNBT(String key) {
        this.item = Utils.removeNBTData(this.item, key);
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

    public static BwItemStack valueOf(String s) {
        return new BwItemStack(XMaterial.matchXMaterial(s).get().parseItem());
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

    public static boolean playerHas(Player player, ItemStack item) {
        ItemStack[] items = player.getInventory().getContents();
        int num = Arrays.stream(items)
                .filter(Objects::nonNull)
                .filter(i -> i.getType() == item.getType() && i.getDurability() == item.getDurability())
                .filter(Utils::hasPluginData)
                .mapToInt(ItemStack::getAmount)
                .sum();
        return num >= item.getAmount();
    }

    public static void removeItem(Player player, ItemStack item) {
        ItemStack[] items = player.getInventory().getContents();
        int amount = item.getAmount();
        for (byte b = 0; b < items.length; b++) {
            ItemStack itemStack = items[b];
            if (itemStack != null && itemStack.getType() == item.getType() && itemStack.getDurability() == item.getDurability() && Utils.hasPluginData(itemStack)) {
                if (itemStack.getAmount() <= amount) {
                    amount -= itemStack.getAmount();
                    items[b] = null;
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    amount = 0;
                }
                if (amount <= 0)
                    break;
            }
        }
        player.getInventory().setContents(items);
    }

    public static int getMissing(Player player, Material material, int amount) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.getType() == material && Utils.hasPluginData(itemStack))
                amount -= itemStack.getAmount();
        }
        return amount;
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
