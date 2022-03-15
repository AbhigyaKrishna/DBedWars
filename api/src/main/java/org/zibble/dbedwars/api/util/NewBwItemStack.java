package org.zibble.dbedwars.api.util;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.LegacyMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.FireworkEffectC;
import org.zibble.dbedwars.api.objects.serializable.LEnchant;
import org.zibble.dbedwars.api.objects.serializable.PotionEffectAT;
import org.zibble.dbedwars.api.util.item.ItemMetaBuilder;
import org.zibble.dbedwars.api.util.json.Json;

import java.util.*;

public class NewBwItemStack {

    private static final String[] BYPASS_CLASS = {
            "CraftMetaBlockState",
            "CraftMetaItem",
            "GlowMetaItem"
    };

    private final XMaterial material;
    private int amount;

    private Message displayName;
    private Message lore;

    private int durability;
    private final Map<XEnchantment, Integer> enchantments;

    private ItemMeta meta;

    public static NewBwItemStack fromJson(Json json) {
        Optional<XMaterial> optmaterial = XMaterial.matchXMaterial(json.get("type").getAsString());
        if (!optmaterial.isPresent()) {
            throw new IllegalArgumentException("Invalid material type: " + json.get("type").getAsString());
        }
        NewBwItemStack item = new NewBwItemStack(optmaterial.get());
        item.setAmount(json.has("amount") ? json.get("amount").getAsInt() : 1);
        item.setDurability(json.has("durability") ? json.get("durability").getAsShort() : 0);

        if (json.has("display-name")) {
            item.setDisplayName(Messaging.get().asConfigMessage(json.get("display-name").getAsString()));
        }
        if (json.has("lore")) {
            JsonArray array = json.get("lore").getAsJsonArray();
            Message lore = Messaging.get().asConfigMessage("");
            String[] lines = new String[array.size()];
            for (int i = 0; i < array.size(); i++) {
                lines[i] = array.get(i).getAsString();
            }
            lore.setMessage(lines);
            item.appendLore(lore);
        }
        if (json.has("enchantments")) {
            JsonArray array = json.get("enchantments").getAsJsonArray();
            for (JsonElement element : array) {
                LEnchant enchant = LEnchant.valueOf(element.getAsString());
                if (enchant != null) {
                    item.addEnchantment(enchant.getEnchantment(), enchant.getLevel());
                }
            }
        }
        if (json.has("flags")) {
            JsonArray array = json.getAsJsonArray("flags");
            for (JsonElement element : array) {
                ItemFlag flag = EnumUtil.matchEnum(element.getAsString(), ItemFlag.values());
                if (flag != null) {
                    item.getMeta().addItemFlags(flag);
                }
            }
        }

        if (!json.has("item-meta")) return item;

        for (String clazz : BYPASS_CLASS) {
            if (item.getMeta().getClass().getSimpleName().equals(clazz)) {
                return item;
            }
        }

        Json metaJson = json.getAsJson("item-meta");

        if (item.getMeta() instanceof SkullMeta) {
            if (metaJson.has("owner")) {
                ((SkullMeta) item.getMeta()).setOwner(metaJson.get("owner").getAsString());
            }
        } else if (item.getMeta() instanceof EnchantmentStorageMeta) {
            if (metaJson.has("stored-enchants")) {
                EnchantmentStorageMeta storage = (EnchantmentStorageMeta) item.getMeta();
                JsonArray array = metaJson.getAsJsonArray("stored-enchants");
                for (JsonElement element : array) {
                    LEnchant enchant = LEnchant.valueOf(element.getAsString());
                    if (enchant != null && enchant.getEnchantment().isSupported()) {
                        storage.addStoredEnchant(enchant.getEnchantment().getEnchant(), enchant.getLevel(), true);
                    }
                }
            }
        } else if (item.getMeta() instanceof LeatherArmorMeta) {
            if (metaJson.has("color")) {
                ((LeatherArmorMeta) item.getMeta()).setColor(org.bukkit.Color.fromRGB(Integer.parseInt(metaJson.get("color").getAsString(), 16)));
            }
        } else if (item.getMeta() instanceof BookMeta) {
            BookMeta bookmeta = (BookMeta) item.getMeta();

            if (metaJson.has("title")) {
                bookmeta.setTitle(metaJson.get("title").getAsString());
            }
            if (metaJson.has("author")) {
                bookmeta.setAuthor(metaJson.get("author").getAsString());
            }
            if (metaJson.has("pages")) {
                JsonArray array = metaJson.getAsJsonArray("pages");
                List<String> pages = new ArrayList<>(array.size());
                for (JsonElement element : array) {
                    pages.add(element.getAsString());
                }
                bookmeta.setPages(pages);
            }

        } else if (item.getMeta() instanceof PotionMeta) {
            PotionMeta potionmeta = (PotionMeta) item.getMeta();
            if (metaJson.has("potion-effects")) {
                JsonArray array = metaJson.getAsJsonArray("potion-effects");
                for (JsonElement element : array) {
                    PotionEffectAT effect = PotionEffectAT.valueOf(element.getAsString());
                    if (effect != null) {
                        potionmeta.addCustomEffect(effect.asBukkit(), true);
                    }
                }
            }
        } else if (item.getMeta() instanceof FireworkEffectMeta) {
            FireworkEffectMeta fireworkmeta = (FireworkEffectMeta) item.getMeta();
            if (metaJson.has("firework-effect")) {
                JsonArray array = metaJson.getAsJsonArray("firework-effect");
                for (JsonElement element : array) {
                    FireworkEffectC effect = FireworkEffectC.valueOf(element.getAsString());
                    if (effect != null) {
                        fireworkmeta.setEffect(effect.getEffect());
                    }
                }
            }
        } else if (item.getMeta() instanceof FireworkMeta) {
            FireworkMeta fireworkMeta = (FireworkMeta) item.getMeta();

            if (metaJson.has("power")) {
                fireworkMeta.setPower(metaJson.get("power").getAsInt());
            }

            if (metaJson.has("firework-effect")) {
                JsonArray array = metaJson.getAsJsonArray("firework-effect");
                for (JsonElement element : array) {
                    FireworkEffectC effect = FireworkEffectC.valueOf(element.getAsString());
                    if (effect != null) {
                        fireworkMeta.addEffect(effect.getEffect());
                    }
                }
            }
        }

        return item;
    }

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
        this.meta = itemStack.getItemMeta().clone();
        this.durability = itemStack.getDurability();
        this.enchantments = new EnumMap<>(XEnchantment.class);
        if (this.meta == null) return;
        if (this.meta.hasDisplayName()) this.displayName = LegacyMessage.from(this.meta.getDisplayName());
        if (this.meta.hasLore()) this.lore = LegacyMessage.from(this.meta.getLore().toArray(new String[0]));
        if (this.meta.hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> entry : this.meta.getEnchants().entrySet()) {
                this.enchantments.put(XEnchantment.matchXEnchantment(entry.getKey()), entry.getValue());
                this.meta.removeEnchant(entry.getKey());
            }
        }

        this.meta.setDisplayName(null);
        this.meta.setLore(null);
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

    public ItemMeta getMeta() {
        return meta;
    }

    public void setMeta(ItemMeta meta) {
        if (this.meta != null && !this.meta.getClass().equals(meta.getClass())) {
            throw new IllegalArgumentException("Invalid meta type");
        }
        this.meta = meta;
    }

    public void appendLore(Message message) {
        if (this.lore == null) {
            this.lore = message;
        } else {
            Component[] components = new Component[message.size() + this.lore.size()];
            System.arraycopy(this.lore.asUnparsedComponent(), 0, components, 0, this.lore.size());
            System.arraycopy(message.asUnparsedComponent(), 0, components, this.lore.size(), message.size());
            AdventureMessage msg = AdventureMessage.from(components);
            msg.addPlaceholders(this.lore.getPlaceholders());
            this.lore = msg;
        }
    }

    public void prependLore(Message message) {
        if (this.lore == null) {
            this.lore = message;
        } else {
            Component[] components = new Component[message.size() + this.lore.size()];
            System.arraycopy(message.asUnparsedComponent(), 0, components, 0, message.size());
            System.arraycopy(this.lore.asUnparsedComponent(), 0, components, message.size(), this.lore.size());
            AdventureMessage msg = AdventureMessage.from(components);
            msg.addPlaceholders(this.lore.getPlaceholders());
            this.lore = msg;
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
        ItemMetaBuilder builder = ItemMetaBuilder.of(this.material, this.meta)
                .displayName(this.displayName.asComponentWithPAPI(player)[0])
                .lore(this.lore.asComponentWithPAPI(player));
        for (Map.Entry<XEnchantment, Integer> entry : this.enchantments.entrySet()) {
            builder.withEnchantment(entry.getKey(), entry.getValue());
        }
        return builder.toItemStack(this.amount);
    }

    public ItemStack asItemStack() {
        ItemMetaBuilder builder = ItemMetaBuilder.of(this.material, this.meta)
                .displayName(this.displayName.asComponent()[0])
                .lore(this.lore.asComponent());
        for (Map.Entry<XEnchantment, Integer> entry : this.enchantments.entrySet()) {
            builder.withEnchantment(entry.getKey(), entry.getValue());
        }
        return builder.toItemStack(this.amount);
    }

}