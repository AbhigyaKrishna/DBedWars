package com.pepedevs.dbedwars.cache;

import com.pepedevs.dbedwars.api.util.NBTUtils;
import com.pepedevs.dbedwars.game.arena.view.shoptest.ShopItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryBackup {

    private final ItemStack[] armorContent = new ItemStack[4];
    private final ItemStack[] content = new ItemStack[36];

    private InventoryBackup() {
    }

    public static InventoryBackup createBackup(Player player) {
        InventoryBackup inventoryBackup = new InventoryBackup();
        ItemStack[] armorContent = player.getInventory().getArmorContents();
        for (byte b = 0; b < 4; b++) {
            inventoryBackup.armorContent[b] = armorContent[b] != null ? armorContent[b].clone() : null;
        }
        ItemStack[] content = player.getInventory().getContents();
        for (byte b = 0; b < 36; b++) {
            inventoryBackup.content[b] = content[b] != null ? content[b].clone() : null;
        }
        return inventoryBackup;
    }

    public ItemStack[] getArmorContent() {
        return this.armorContent;
    }

    public ItemStack[] getContent() {
        return this.content;
    }

    public void applyInventory(Player player) {
        player.getInventory().setArmorContents(this.armorContent);
        player.getInventory().setContents(this.content);
    }

    public List<ItemStack> getPermanents() {
        List<ItemStack> permanents = new ArrayList<>();
        for (ItemStack itemStack : this.armorContent) {
            if (NBTUtils.hasNBTData(itemStack, ShopItem.PERMANENT_KEY)) {
                permanents.add(itemStack);
            }
        }

        for (ItemStack itemStack : this.content) {
            if (NBTUtils.hasNBTData(itemStack, ShopItem.PERMANENT_KEY)) {
                permanents.add(itemStack);
            }
        }

        return permanents;
    }

    public void applyPermanents(Player player) {
        for (byte b = 0; b < 4; b++) {
            if (NBTUtils.hasNBTData(this.armorContent[b], ShopItem.PERMANENT_KEY)) {
                switch (b) {
                    case 0:
                        player.getInventory().setHelmet(this.armorContent[b]);
                        break;
                    case 1:
                        player.getInventory().setChestplate(this.armorContent[b]);
                        break;
                    case 2:
                        player.getInventory().setLeggings(this.armorContent[b]);
                        break;
                    case 3:
                        player.getInventory().setBoots(this.armorContent[b]);
                        break;
                }
            }
        }

        for (ItemStack itemStack : this.content) {
            if (NBTUtils.hasNBTData(itemStack, ShopItem.PERMANENT_KEY)) {
                player.getInventory().addItem(itemStack);
            }
        }
    }

}
