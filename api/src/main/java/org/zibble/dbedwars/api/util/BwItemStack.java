package org.zibble.dbedwars.api.util;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.LegacyMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.nms.NBTItem;
import org.zibble.dbedwars.api.objects.serializable.FireworkEffectC;
import org.zibble.dbedwars.api.objects.serializable.LEnchant;
import org.zibble.dbedwars.api.objects.serializable.PotionEffectAT;
import org.zibble.dbedwars.api.util.item.ItemMetaBuilder;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.api.util.nbt.NBT;
import org.zibble.dbedwars.api.util.nbt.NBTType;
import org.zibble.dbedwars.api.util.nbt.serializer.JsonNbtSerializer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BwItemStack implements Cloneable {

    public static final Pattern PATTERN = Pattern.compile("^(?:(?<amount>\\d*)::)?(?<type>[a-zA-Z0-9_\\-]+?)(?:::(?<data>\\d+))?$");
    public static final Pattern JSON_MATCHER = Pattern.compile("^json::(?<item>.+?\\..+?)(?:::(?<amount>\\d*))?$");
    private static final String[] BYPASS_CLASS = {
            "CraftMetaBlockState",
            "CraftMetaItem",
            "GlowMetaItem"
    };
    private final Set<LEnchant> enchantments;
    private XMaterial material;
    private int amount;
    private Message displayName;
    private Message lore;
    private int data;
    private Map<String, NBT> nbt;

    private ItemMeta meta;

    public BwItemStack(XMaterial material) {
        this(material, 1);
    }

    public BwItemStack(XMaterial material, int amount) {
        if (!material.isSupported()) {
            throw new IllegalArgumentException("Material " + material.name() + " is not supported");
        }
        this.material = material;
        this.amount = amount;
        this.enchantments = new HashSet<>();
        this.data = -1;
        this.meta = material.parseItem().getItemMeta();
        this.nbt = new HashMap<>();
    }

    public BwItemStack(ItemStack item) {
        this(item, item.getAmount());
    }

    public BwItemStack(ItemStack itemStack, int amount) {
        this.material = XMaterial.matchXMaterial(itemStack);
        this.amount = amount;
        this.meta = itemStack.getItemMeta();
        this.data = itemStack.getDurability();
        this.enchantments = new HashSet<>();
        if (this.meta == null) return;
        if (this.meta.hasDisplayName()) this.displayName = LegacyMessage.from(this.meta.getDisplayName());
        if (this.meta.hasLore()) this.lore = LegacyMessage.from(this.meta.getLore());
        if (this.meta.hasEnchants()) {
            for (Map.Entry<Enchantment, Integer> entry : this.meta.getEnchants().entrySet()) {
                this.enchantments.add(LEnchant.of(XEnchantment.matchXEnchantment(entry.getKey()), entry.getValue()));
                this.meta.removeEnchant(entry.getKey());
            }
        }
        this.nbt = DBedWarsAPI.getApi().getNMS().getNBTItem(itemStack).getTags();

        this.meta.setDisplayName(null);
        this.meta.setLore(null);
    }

    public static BwItemStack valueOf(String str, Placeholder... placeholders) {
        Messaging messaging = Messaging.get();
        Matcher matcher = JSON_MATCHER.matcher(str);
        if (matcher.matches()) {
            String item = messaging.setPlaceholders(matcher.group("item"), placeholders);

            BwItemStack configuredItem = DBedWarsAPI.getApi().getConfiguredItem(item, placeholders);
            String amt = matcher.group("amount");
            if (configuredItem != null && amt != null) {
                int num = NumberUtils.toInt(messaging.setPlaceholders(amt, placeholders));
                if (num > 0) {
                    configuredItem.setAmount(num);
                }
            }
            return configuredItem;
        }

        matcher = PATTERN.matcher(str);
        if (matcher.matches()) {
            Optional<XMaterial> material = XMaterial.matchXMaterial(matcher.group("type"));
            if (!material.isPresent() || !material.get().isSupported()) return null;
            BwItemStack itemStack = new BwItemStack(material.get());
            String amt = matcher.group("amount");
            if (amt != null) {
                itemStack.setAmount(NumberUtils.toInt(messaging.setPlaceholders(amt, placeholders), 1));
            }
            String data = matcher.group("data");
            if (data != null) {
                itemStack.setData(NumberUtils.toShort(messaging.setPlaceholders(data, placeholders), (short) 0));
            }
            return itemStack;
        }

        return null;
    }

    public static BwItemStack fromJson(Json json, Placeholder... placeholders) {
        Messaging messaging = Messaging.get();
        Optional<XMaterial> optmaterial = XMaterial.matchXMaterial(messaging.setPlaceholders(json.get("type").getAsString(), placeholders));
        if (!optmaterial.isPresent()) {
            throw new IllegalArgumentException("Invalid material type: " + json.get("type").getAsString());
        }
        BwItemStack item = new BwItemStack(optmaterial.get());
        if (json.has("amount")) {
            item.setAmount(NumberUtils.toInt(messaging.setPlaceholders(json.get("amount").getAsString(), placeholders), 1));
        }
        if (json.has("data")) {
            item.setData(NumberUtils.toInt(messaging.setPlaceholders(json.get("data").getAsString(), placeholders), 0));
        }

        if (json.has("display-name")) {
            Message message = Messaging.get().asConfigMessage(json.get("display-name").getAsString());
            message.addPlaceholders(placeholders);
            item.setDisplayName(message);
        }
        if (json.has("lore")) {
            JsonArray array = json.get("lore").getAsJsonArray();
            Message lore = Messaging.get().asConfigMessage("");
            String[] lines = new String[array.size()];
            for (int i = 0; i < array.size(); i++) {
                lines[i] = array.get(i).getAsString();
            }
            lore.setMessage(lines);
            lore.addPlaceholders(placeholders);
            item.appendLore(lore);
        }
        if (json.has("enchantments")) {
            JsonArray array = json.get("enchantments").getAsJsonArray();
            for (JsonElement element : array) {
                LEnchant enchant = LEnchant.valueOf(messaging.setPlaceholders(element.getAsString(), placeholders));
                if (enchant != null) {
                    item.addEnchantment(enchant);
                }
            }
        }
        if (json.has("flags")) {
            JsonArray array = json.getAsJsonArray("flags");
            for (JsonElement element : array) {
                ItemFlag flag = EnumUtil.matchEnum(messaging.setPlaceholders(element.getAsString(), placeholders), ItemFlag.values());
                if (flag != null) {
                    item.getMeta().addItemFlags(flag);
                }
            }
        }
        if (json.has("nbt")) {
            Json array = json.getAsJson("nbt");
            for (Map.Entry<String, JsonElement> entry : array.entrySet()) {
                item.addNbt(entry.getKey(), JsonNbtSerializer.deserialize(entry.getValue()));
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
                ((SkullMeta) item.getMeta()).setOwner(messaging.setPlaceholders(metaJson.get("owner").getAsString(), placeholders));
            }
        } else if (item.getMeta() instanceof EnchantmentStorageMeta && metaJson.has("stored-enchants")) {
            EnchantmentStorageMeta storage = (EnchantmentStorageMeta) item.getMeta();
            JsonArray array = metaJson.getAsJsonArray("stored-enchants");
            for (JsonElement element : array) {
                LEnchant enchant = LEnchant.valueOf(messaging.setPlaceholders(element.getAsString(), placeholders));
                if (enchant != null && enchant.getEnchantment().isSupported()) {
                    storage.addStoredEnchant(enchant.getEnchantment().getEnchant(), enchant.getLevel(), true);
                }
            }
        } else if (item.getMeta() instanceof LeatherArmorMeta) {
            if (metaJson.has("color")) {
                ((LeatherArmorMeta) item.getMeta()).setColor(org.bukkit.Color.fromRGB(Integer.parseInt(
                        messaging.setPlaceholders(metaJson.get("color").getAsString(), placeholders), 16)));
            }
        } else if (item.getMeta() instanceof BookMeta) {
            BookMeta bookmeta = (BookMeta) item.getMeta();

            if (metaJson.has("title")) {
                bookmeta.setTitle(messaging.setPlaceholders(metaJson.get("title").getAsString(), placeholders));
            }
            if (metaJson.has("author")) {
                bookmeta.setAuthor(messaging.setPlaceholders(metaJson.get("author").getAsString(), placeholders));
            }
            if (metaJson.has("pages")) {
                JsonArray array = metaJson.getAsJsonArray("pages");
                List<String> pages = new ArrayList<>(array.size());
                for (JsonElement element : array) {
                    pages.add(messaging.setPlaceholders(element.getAsString(), placeholders));
                }
                bookmeta.setPages(pages);
            }

        } else if (item.getMeta() instanceof PotionMeta) {
            PotionMeta potionmeta = (PotionMeta) item.getMeta();
            if (metaJson.has("potion-effects")) {
                JsonArray array = metaJson.getAsJsonArray("potion-effects");
                for (JsonElement element : array) {
                    PotionEffectAT effect = PotionEffectAT.valueOf(messaging.setPlaceholders(element.getAsString(), placeholders));
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
                    FireworkEffectC effect = FireworkEffectC.valueOf(messaging.setPlaceholders(element.getAsString(), placeholders));
                    if (effect != null) {
                        fireworkmeta.setEffect(effect.getEffect());
                    }
                }
            }
        } else if (item.getMeta() instanceof FireworkMeta) {
            FireworkMeta fireworkMeta = (FireworkMeta) item.getMeta();

            if (metaJson.has("power")) {
                fireworkMeta.setPower(NumberUtils.toInt(messaging.setPlaceholders(metaJson.get("power").getAsString(), placeholders)));
            }

            if (metaJson.has("firework-effect")) {
                JsonArray array = metaJson.getAsJsonArray("firework-effect");
                for (JsonElement element : array) {
                    FireworkEffectC effect = FireworkEffectC.valueOf(messaging.setPlaceholders(element.getAsString(), placeholders));
                    if (effect != null) {
                        fireworkMeta.addEffect(effect.getEffect());
                    }
                }
            }
        }

        return item;
    }

    public XMaterial getType() {
        return material;
    }

    public void setType(XMaterial material) {
        this.material = material;
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
            System.arraycopy(this.lore.asRawComponent(), 0, components, 0, this.lore.size());
            System.arraycopy(message.asRawComponent(), 0, components, this.lore.size(), message.size());
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
            System.arraycopy(message.asRawComponent(), 0, components, 0, message.size());
            System.arraycopy(this.lore.asRawComponent(), 0, components, message.size(), this.lore.size());
            AdventureMessage msg = AdventureMessage.from(components);
            msg.addPlaceholders(this.lore.getPlaceholders());
            this.lore = msg;
        }
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Set<LEnchant> getEnchantments() {
        return this.enchantments;
    }

    public boolean hasEnchant(XEnchantment enchantment) {
        for (LEnchant enchant : this.enchantments) {
            if (enchant.getEnchantment().equals(enchantment)) return true;
        }
        return false;
    }

    public void addEnchantment(LEnchant enchantment) {
        this.enchantments.add(enchantment);
    }

    public void removeEnchantment(XEnchantment enchantment) {
        this.enchantments.removeIf(enchant -> enchant.getEnchantment().equals(enchantment));
    }

    public void removeAllEnchantments() {
        this.enchantments.clear();
    }

    public int getEnchantLevel(XEnchantment enchantment) {
        for (LEnchant enchant : this.enchantments) {
            if (enchant.getEnchantment().equals(enchantment)) return enchant.getLevel();
        }
        return 0;
    }

    public void addNbt(String key, NBT value) {
        this.nbt.put(key, value);
    }

    public <T extends NBT> T getNbt(String key, NBTType<T> type) {
        return (T) this.nbt.get(key);
    }

    public void removeNbt(String key) {
        this.nbt.remove(key);
    }

    public void removeAllNbt() {
        this.nbt.clear();
    }

    public Map<String, NBT> getNbt() {
        return Collections.unmodifiableMap(this.nbt);
    }

    public boolean hasNbt(String key) {
        return this.nbt.containsKey(key);
    }

    public ItemStack asItemStack(Player player) {
        ItemMetaBuilder builder = ItemMetaBuilder.of(this.material, this.meta)
                .displayName(this.displayName.asComponentWithPAPI(player)[0])
                .lore(this.lore.asComponentWithPAPI(player));
        for (LEnchant enchant : this.enchantments) {
            if (!enchant.getEnchantment().isSupported()) continue;
            builder.withEnchantment(enchant.getEnchantment(), enchant.getLevel());
        }
        ItemStack item = builder.toItemStack(this.amount);
        if (this.data > 0) {
            item.setDurability((short) this.data);
        }
        NBTItem nbtItem = DBedWarsAPI.getApi().getNMS().getNBTItem(item);
        nbtItem.applyTags(this.nbt);
        return nbtItem.getItem();
    }

    public ItemStack asItemStack() {
        ItemMetaBuilder builder = ItemMetaBuilder.of(this.material, this.meta)
                .displayName(this.displayName.asComponent()[0])
                .lore(this.lore.asComponent());
        for (LEnchant enchant : this.enchantments) {
            if (!enchant.getEnchantment().isSupported()) continue;
            builder.withEnchantment(enchant.getEnchantment(), enchant.getLevel());
        }
        ItemStack item = builder.toItemStack(this.amount);
        if (this.data > 0) {
            item.setDurability((short) this.data);
        }
        NBTItem nbtItem = DBedWarsAPI.getApi().getNMS().getNBTItem(item);
        nbtItem.applyTags(this.nbt);
        return nbtItem.getItem();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BwItemStack that = (BwItemStack) o;
        return amount == that.amount && this.isSimilar(that);
    }

    public boolean isSimilar(BwItemStack item) {
        if (this == item) return true;
        return data == item.data
                && material == item.material
                && ((item.displayName == null && this.displayName == null) || displayName.equals(item.displayName))
                && ((item.lore == null && this.lore == null) || lore.equals(item.lore))
                && enchantments.equals(item.enchantments)
                && nbt.equals(item.nbt)
                && meta.equals(item.meta);
    }

    @Override
    public BwItemStack clone() {
        BwItemStack returnVal = new BwItemStack(this.material, this.amount);
        returnVal.displayName = this.displayName.clone();
        returnVal.lore = this.lore.clone();
        returnVal.meta = this.meta.clone();
        returnVal.data = this.data;
        this.nbt.forEach((key, value) -> returnVal.nbt.put(key, value.clone()));
        this.enchantments.forEach(enchant -> returnVal.enchantments.add(enchant.clone()));
        return returnVal;
    }

}