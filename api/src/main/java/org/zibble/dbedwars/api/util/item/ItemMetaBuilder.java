package org.zibble.dbedwars.api.util.item;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zibble.dbedwars.api.adventure.AdventureUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Represents a class for building {@link ItemMeta} */
public final class ItemMetaBuilder {

    private final Material material;
    private ItemMeta result;

    /**
     * Constructs the ItemMetaBuilder.
     *
     * <p>
     *
     * @param material Material for getting ItemMeta
     */
    public ItemMetaBuilder(Material material) {
        this.material = material;
        this.result = Bukkit.getItemFactory().getItemMeta(this.material);
        if (this.result == null) {
            throw new IllegalArgumentException("Unsupported Material: " + material.name());
        }
    }

    /**
     * Constructs the ItemMetaBuilder.
     *
     * <p>
     *
     * @param stack ItemStack for getting ItemMeta
     */
    public ItemMetaBuilder(ItemStack stack) {
        this.material = stack.getType();
        this.result = stack.getItemMeta();
        if (this.result == null) {
            Bukkit.getItemFactory().getItemMeta(this.material);
        }
        if (this.result == null) {
            throw new IllegalArgumentException("Unsupported Material: " + material.name());
        }
    }

    /**
     * Returns the instance of {@link ItemMetaBuilder} for the given ItemStack.
     *
     * <p>
     *
     * @param stack ItemStack to get ItemMeta of
     * @return {@link ItemMetaBuilder} instance
     */
    public static ItemMetaBuilder of(ItemStack stack) {
        return new ItemMetaBuilder(stack);
    }

    /**
     * Returns the instance of {@link ItemMetaBuilder} for the given ItemStack.
     *
     * <p>
     *
     * @param material Material to get ItemMeta of
     * @return {@link ItemMetaBuilder} instance
     */
    public static ItemMetaBuilder of(XMaterial material) {
        return of(material.parseItem());
    }

    /**
     * Returns the instance of {@link ItemMetaBuilder} for the given Material and ItemMeta.
     *
     * <p>
     *
     * @param material Material of ItemStack
     * @param meta Meta of ItemStack
     * @return {@link ItemMetaBuilder} instance
     */
    public static ItemMetaBuilder of(Material material, ItemMeta meta) {
        ItemMetaBuilder builder = new ItemMetaBuilder(material);
        if (meta != null) {
            builder.result = meta;
        }
        return builder;
    }

    /**
     * Sets the display name in ItemMeta.
     *
     * <p>
     *
     * @param display_name Display name
     * @return This Object, for chaining
     */
    public ItemMetaBuilder withDisplayName(String display_name) {
        result.setDisplayName(display_name);
        return this;
    }

    /**
     * Sets the display name in ItemMeta.
     *
     * <p>
     *
     * @param display_name Display name component
     * @return This Object, for chaining
     */
    public ItemMetaBuilder displayName(Component display_name) {
        return this.withDisplayName(AdventureUtils.toVanillaString(display_name));
    }

    /**
     * Sets the lore in ItemMeta.
     *
     * <p>
     *
     * @param lore Lore
     * @return This Object, for chaining
     */
    public ItemMetaBuilder withLore(List<String> lore) {
        result.setLore(lore);
        return this;
    }

    /**
     * Sets the lore in ItemMeta.
     *
     * <p>
     *
     * @param lore Lore components
     * @return This Object, for chaining
     */
    public ItemMetaBuilder lore(List<Component> lore) {
        List<String> stringLore = new ArrayList<>();
        for (Component component : lore) {
            stringLore.add(AdventureUtils.toVanillaString(component));
        }
        return this.withLore(stringLore);
    }

    /**
     * Sets the lore in ItemMeta.
     *
     * <p>
     *
     * @param lore Lore
     * @return This Object, for chaining
     */
    public ItemMetaBuilder withLore(String... lore) {
        return withLore(Arrays.asList(lore));
    }

    /**
     * Sets the lore in ItemMeta.
     *
     * <p>
     *
     * @param lore Lore components
     * @return This Object, for chaining
     */
    public ItemMetaBuilder lore(Component... lore) {
        String[] str = new String[lore.length];
        for (int i = 0; i < lore.length; i++) {
            str[i] = AdventureUtils.toVanillaString(lore[i]);
        }
        return this.withLore(str);
    }

    /**
     * Append line to the lore in ItemMeta.
     *
     * <p>
     *
     * @param line Line to append to lore
     * @return This Object, for chaining
     */
    public ItemMetaBuilder appendToLore(String line) {
        List<String> lore = result.getLore();
        if (lore == null) {
            return withLore(line);
        } else {
            lore.add(line);
            return withLore(lore);
        }
    }

    /**
     * Append line to the lore in ItemMeta.
     *
     * <p>
     *
     * @param line Line component to append to lore
     * @return This Object, for chaining
     */
    public ItemMetaBuilder appendLore(Component line) {
        return this.appendToLore(AdventureUtils.toVanillaString(line));
    }

    /**
     * Remove lore line from ItemMeta.
     *
     * <p>
     *
     * @param line Line to remove from lore
     * @return This Object, for chaining
     */
    public ItemMetaBuilder removeFromLore(String line) {
        List<String> lore = result.getLore();
        if (lore != null) {
            lore.remove(line);
            return withLore(lore);
        }
        return this;
    }

    /**
     * Remove lore line from ItemMeta.
     *
     * <p>
     *
     * @param line Line component to remove from lore
     * @return This Object, for chaining
     */
    public ItemMetaBuilder removeFromLore(Component line) {
        return this.removeFromLore(AdventureUtils.toVanillaString(line));
    }

    /**
     * Sets the enchantment in ItemMeta.
     *
     * <p>
     *
     * @param enchantment Enchantment
     * @param level Level of enchantment
     * @param ignore_max_level Ignore max level cap?
     * @return This Object, for chaining
     */
    public ItemMetaBuilder withEnchantment(
            Enchantment enchantment, int level, boolean ignore_max_level) {
        result.addEnchant(enchantment, level, ignore_max_level);
        return this;
    }

    /**
     * Sets the enchantment in ItemMeta.
     *
     * <p>
     *
     * @param enchantment Enchantment
     * @param level Level of enchantment
     * @return This Object, for chaining
     */
    public ItemMetaBuilder withEnchantment(Enchantment enchantment, int level) {
        return withEnchantment(enchantment, level, true);
    }

    /**
     * Sets the enchantment in ItemMeta.
     *
     * <p>
     *
     * @param enchantment Enchantment
     * @param level Level of enchantment
     * @return This Object, for chaining
     */
    public ItemMetaBuilder withEnchantment(XEnchantment enchantment, int level) {
        return withEnchantment(enchantment.getEnchant(), level, true);
    }


    /**
     * Sets the enchantment in ItemMeta.
     *
     * <p>
     *
     * @param enchantment Enchantment
     * @return This Object, for chaining
     */
    public ItemMetaBuilder withEnchantment(Enchantment enchantment) {
        return withEnchantment(enchantment, 0);
    }

    /**
     * Remove the enchantment in ItemMeta.
     *
     * <p>
     *
     * @param enchantment Enchantment to remove
     * @return This Object, for chaining
     */
    public ItemMetaBuilder withoutEnchantment(Enchantment enchantment) {
        result.removeEnchant(enchantment);
        return this;
    }

    /**
     * Sets the Item Flag in ItemMeta.
     *
     * <p>
     *
     * @param flag Item Flag
     * @return This Object, for chaining
     */
    public ItemMetaBuilder withItemFlags(ItemFlag... flag) {
        result.addItemFlags(flag);
        return this;
    }

    /**
     * Remove the Item Flag in ItemMeta.
     *
     * <p>
     *
     * @param flag Item Flag
     * @return This Object, for chaining
     */
    public ItemMetaBuilder withoutItemFlags(ItemFlag... flag) {
        result.removeItemFlags(flag);
        return this;
    }

    public ItemMetaBuilder setMeta(ItemMeta meta) {
        this.result = meta;
        return this;
    }

    /**
     * Create the {@link ItemMeta} with the given configuration.
     *
     * <p>
     *
     * @return Created {@link ItemMeta}
     */
    public ItemMeta build() {
        return result;
    }

    /**
     * Returns ItemStack for the given configuration of ItemMeta.
     *
     * <p>
     *
     * @param amount Amount of ItemStack
     * @return {@link ItemStack}
     */
    public ItemStack toItemStack(int amount) {
        return applyTo(new ItemStack(material, amount));
    }

    /**
     * Returns ItemStack for the given configuration of ItemMeta.
     *
     * <p>
     *
     * @return {@link ItemStack}
     */
    public ItemStack toItemStack() {
        return applyTo(new ItemStack(material, 1));
    }

    /**
     * Applies the given ItemMeta configuration to the given ItemStack.
     *
     * <p>
     *
     * @param stack ItemStack to apply to
     * @return {@link ItemStack}
     */
    public ItemStack applyTo(ItemStack stack) {
        if (stack == null) {
            return null;
        }

        if (stack.getType() != material) {
            return stack;
        }

        stack.setItemMeta(result);
        return stack;
    }

}
