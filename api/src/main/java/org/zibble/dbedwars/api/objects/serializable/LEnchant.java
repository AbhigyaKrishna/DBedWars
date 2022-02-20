package org.zibble.dbedwars.api.objects.serializable;

import com.pepedevs.radium.utils.xseries.XEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class LEnchant implements Cloneable {

    private XEnchantment enchantment;
    private int level;

    public LEnchant(Enchantment enchantment) {
        this(XEnchantment.matchXEnchantment(enchantment));
    }

    public LEnchant(XEnchantment enchantment) {
        this(enchantment, 1);
    }

    public LEnchant(Enchantment enchantment, int level) {
        this(XEnchantment.matchXEnchantment(enchantment), level);
    }

    public LEnchant(XEnchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public static LEnchant valueOf(String str) {
        String[] s = str.split(":");
        Optional<XEnchantment> opt = XEnchantment.matchXEnchantment(s[0]);
        if (!opt.isPresent()) return null;

        int level = 1;
        if (s.length == 2) {
            try {
                level = Integer.parseInt(s[1]);
            } catch (NumberFormatException ignored) {
            }
        }

        return new LEnchant(opt.get(), level);
    }

    public XEnchantment getEnchantment() {
        return enchantment;
    }

    public void setEnchantment(XEnchantment enchantment) {
        this.enchantment = enchantment;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean apply(ItemStack item) {
        if (!this.enchantment.isSupported()) {
            return false;
        }

        item.addEnchantment(this.enchantment.getEnchant(), this.level);
        return true;
    }

    public boolean applyUnsafe(ItemStack item) {
        if (!this.enchantment.isSupported()) {
            return false;
        }

        item.addUnsafeEnchantment(this.enchantment.getEnchant(), this.level);
        return true;
    }

    @Override
    public String toString() {
        return this.enchantment.name() + ":" + this.level;
    }

    @Override
    public LEnchant clone() {
        try {
            return (LEnchant) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
