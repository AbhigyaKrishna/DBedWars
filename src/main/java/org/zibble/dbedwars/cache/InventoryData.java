package org.zibble.dbedwars.cache;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;

public class InventoryData {

    private final BwItemStack[] armorContent = new BwItemStack[4];
    private final BwItemStack[] content = new BwItemStack[36];

    public static InventoryData create(Player player) {
        InventoryData inventory = new InventoryData();
        ItemStack[] armorContent = player.getInventory().getArmorContents();
        for (byte b = 0; b < 4; b++) {
            inventory.armorContent[b] = armorContent[b] != null ? new BwItemStack(armorContent[b]) : null;
        }
        ItemStack[] content = player.getInventory().getContents();
        for (byte b = 0; b < 36; b++) {
            inventory.content[b] = content[b] != null ? new BwItemStack(content[b]) : null;
        }
        return inventory;
    }

    public void setHelmet(BwItemStack item) {
        this.armorContent[0] = item;
    }

    public void setChestPlate(BwItemStack item) {
        this.armorContent[1] = item;
    }

    public void setLeggings(BwItemStack item) {
        this.armorContent[2] = item;
    }

    public void setBoots(BwItemStack item) {
        this.armorContent[3] = item;
    }

    public void setItem(int slot, BwItemStack item) {
        this.content[slot] = item;
    }

    public void addItem(BwItemStack item) {
        for (byte b = 0; b < 36; b++) {
            if (this.content[b] == null) {
                this.content[b] = item;
                return;
            }
        }
    }

    public BwItemStack[] getArmorContent() {
        return this.armorContent;
    }

    public BwItemStack[] getContent() {
        return this.content;
    }

    public void applyInventory(Player player) {
        ItemStack[] armorContent = new ItemStack[4];
        for (byte b = 0; b < 4; b++) {
            armorContent[b] = this.armorContent[b] != null ? this.armorContent[b].asItemStack(player) : null;
        }
        player.getInventory().setArmorContents(armorContent);
        ItemStack[] content = new ItemStack[36];
        for (byte b = 0; b < 36; b++) {
            content[b] = this.content[b] != null ? this.content[b].asItemStack(player) : null;
        }
        player.getInventory().setContents(content);
    }

}
