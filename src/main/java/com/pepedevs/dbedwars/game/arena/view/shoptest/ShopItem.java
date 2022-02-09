package com.pepedevs.dbedwars.game.arena.view.shoptest;

import com.pepedevs.dbedwars.api.version.Version;
import com.pepedevs.radium.utils.xseries.XItemStack;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.dbedwars.api.util.NBTUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ShopItem implements com.pepedevs.dbedwars.api.game.view.ShopItem {

    public static final String PERMANENT_KEY = "permanent";

    private final String key;
    private final BwItemStack item;
    private final Set<ItemStack> cost;
    private boolean autoEquip;
    private boolean colorable;
    private int slot;

    public ShopItem(String key, BwItemStack item) {
        this.key = key;
        this.item = item;
        this.cost = new HashSet<>();
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public Set<ItemStack> getCost() {
        return this.cost;
    }

    @Override
    public BwItemStack getItem() {
        return this.item;
    }

    @Override
    public boolean isCostFullFilled(Player player) {
        return this.getCost().stream().allMatch(i -> BwItemStack.playerHas(player, i));
    }

    @Override
    public void onPurchase(ArenaPlayer player) {
        if (this.isColorable()) this.color(player.getTeam().getColor());
        if (this.isAutoEquip()) this.equip(player.getPlayer());
        else XItemStack.giveOrDrop(player.getPlayer(), this.getItem().toItemStack());
    }

    @Override
    public boolean isPermanent() {
        return NBTUtils.hasNBTData(this.item.toItemStack(), PERMANENT_KEY);
    }

    @Override
    public void setPermanent(boolean flag) {
        if (flag) this.item.addNBT(PERMANENT_KEY, true);
        else this.item.removeNBT(PERMANENT_KEY);
    }

    @Override
    public boolean isAutoEquip() {
        return this.autoEquip;
    }

    @Override
    public void setAutoEquip(boolean flag) {
        this.autoEquip = flag;
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public void equip(Player player) {
        player.getInventory().setItem(this.getSlot(), this.getItem().toItemStack());
    }

    @Override
    public boolean isColorable() {
        return this.colorable;
    }

    @Override
    public void setColorable(boolean flag) {
        this.colorable = flag;
    }

    @Override
    public void color(Color color) {
        if (color == null) return;

        if (this.item.toItemStack() instanceof org.bukkit.material.Colorable) {
            if (DBedwars.getInstance().getServerVersion().isOlderEquals(Version.v1_8_R3)) {
                this.item.setData(color.getData());
                return;
            }
            ((org.bukkit.material.Colorable) this.item.toItemStack()).setColor(color.getDyeColor());
        }
    }
}
