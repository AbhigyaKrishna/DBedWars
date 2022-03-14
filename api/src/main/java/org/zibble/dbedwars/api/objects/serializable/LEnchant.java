package org.zibble.dbedwars.api.objects.serializable;

import com.cryptomorin.xseries.XEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LEnchant implements Cloneable {

    private static final Pattern PATTERN = Pattern.compile("^(?<name>[a-zA-Z0-9_\\-]+)(?:::(?<level>[+-]?\\d*\\.?\\d*|max))?$");

    private XEnchantment enchantment;
    private int level;

    public static LEnchant of(Enchantment enchantment) {
        return of(XEnchantment.matchXEnchantment(enchantment));
    }

    public static LEnchant of(XEnchantment enchantment) {
        return of(enchantment, 1);
    }

    public static LEnchant of(Enchantment enchantment, int level) {
        return of(XEnchantment.matchXEnchantment(enchantment), level);
    }

    public static LEnchant of(XEnchantment enchantment, int level) {
        return new LEnchant(enchantment, level);
    }

    private LEnchant(XEnchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public static LEnchant valueOf(String str) {
        Matcher matcher = PATTERN.matcher(str);
        if (matcher.matches()) {
            XEnchantment enchantment = XEnchantment.matchXEnchantment(matcher.group("name")).orElse(null);
            if (enchantment == null) return null;

            int level = matcher.groupCount() > 1 ? (int) Double.parseDouble(matcher.group("level")) : 1;

            return new LEnchant(enchantment, level);
        }
        return null;
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
        return new LEnchant(this.enchantment, this.level);
    }
}
