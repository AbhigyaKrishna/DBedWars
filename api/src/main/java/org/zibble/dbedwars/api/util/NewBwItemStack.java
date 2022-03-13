package org.zibble.dbedwars.api.util;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.LegacyMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.item.ItemMetaBuilder;

import java.util.EnumMap;
import java.util.Map;

public class NewBwItemStack {

    private final XMaterial material;
    private int amount;

    private Message displayName;
    private Message lore;

    private int durability;
    private final Map<XEnchantment, Integer> enchantments;

    public NewBwItemStack(XMaterial material) {
        this(material, 1);
    }

    public NewBwItemStack(XMaterial material, int amount) {
        this.material = material;
        this.amount = amount;
        this.enchantments = new EnumMap<>(XEnchantment.class);
        this.durability = -1;
    }

    public NewBwItemStack(ItemStack itemStack) {
        this.material = XMaterial.matchXMaterial(itemStack);
        this.amount = itemStack.getAmount();
        ItemMeta meta = itemStack.getItemMeta();
        this.durability = itemStack.getDurability();
        this.enchantments = new EnumMap<>(XEnchantment.class);
        if (meta == null) return;
        if (meta.hasDisplayName()) this.displayName = LegacyMessage.from(meta.getDisplayName());
        if (meta.hasLore()) this.lore = LegacyMessage.from(meta.getLore().toArray(new String[0]));
        if (meta.hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
                this.enchantments.put(XEnchantment.matchXEnchantment(entry.getKey()), entry.getValue());
            }
        }
    }

    public XMaterial getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Message getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Message displayName) {
        this.displayName = displayName;
    }

    public Message getLore() {
        return lore;
    }

    public void setLore(Message lore) {
        this.lore = lore;
    }

    public void appendLore(Message message) {
        if (this.lore == null) {
            this.lore = message;
        } else {
            Component[] components = new Component[message.asComponent().length + this.lore.asComponent().length];
            System.arraycopy(this.lore.asComponent(), 0, components, 0, this.lore.asComponent().length);
            System.arraycopy(message.asComponent(), 0, components, this.lore.asComponent().length, message.asComponent().length);
            this.lore = AdventureMessage.from(components);
        }
    }

    public void prependLore(Message message) {
        if (this.lore == null) {
            this.lore = message;
        } else {
            Component[] components = new Component[message.asComponent().length + this.lore.asComponent().length];
            System.arraycopy(message.asComponent(), 0, components, 0, message.asComponent().length);
            System.arraycopy(this.lore.asComponent(), 0, components, message.asComponent().length, this.lore.asComponent().length);
            this.lore = AdventureMessage.from(components);
        }
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public Map<XEnchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public boolean hasEnchant(XEnchantment enchantment) {
        return enchantments.containsKey(enchantment);
    }

    public void addEnchantment(XEnchantment enchantment, int level) {
        enchantments.put(enchantment, level);
    }

    public void removeEnchantment(XEnchantment enchantment) {
        enchantments.remove(enchantment);
    }

    public void removeAllEnchantments() {
        enchantments.clear();
    }

    public int getEnchantLevel(XEnchantment enchantment) {
        return enchantments.getOrDefault(enchantment, 0);
    }

    public ItemStack asItemStack(Player player) {
        ItemMetaBuilder builder = ItemMetaBuilder.of(this.material)
                .displayName(this.displayName.asComponentWithPAPI(player)[0])
                .lore(this.lore.asComponentWithPAPI(player));
        for (Map.Entry<XEnchantment, Integer> entry : this.enchantments.entrySet()) {
            builder.withEnchantment(entry.getKey(), entry.getValue());
        }
        return builder.toItemStack(this.amount);
    }

    public ItemStack asItemStack() {
        ItemMetaBuilder builder = ItemMetaBuilder.of(this.material)
                .displayName(this.displayName.asComponent()[0])
                .lore(this.lore.asComponent());
        for (Map.Entry<XEnchantment, Integer> entry : this.enchantments.entrySet()) {
            builder.withEnchantment(entry.getKey(), entry.getValue());
        }
        return builder.toItemStack(this.amount);
    }

}