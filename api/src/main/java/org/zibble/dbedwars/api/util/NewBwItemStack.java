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
import org.zibble.dbedwars.api.nms.NBTItem;
import org.zibble.dbedwars.api.objects.serializable.FireworkEffectC;
import org.zibble.dbedwars.api.objects.serializable.LEnchant;
import org.zibble.dbedwars.api.objects.serializable.PotionEffectAT;
import org.zibble.dbedwars.api.util.item.ItemMetaBuilder;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.api.util.nbt.*;
import org.zibble.dbedwars.api.util.nbt.serializer.JsonNbtSerializer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewBwItemStack {

    private static final Pattern PATTERN = Pattern.compile("^(?:(?<amount>\\d*)::)?(?<type>[a-zA-Z0-9_\\-]+?)(?:::(?<data>\\d+))?$");
    private static final String[] BYPASS_CLASS = {
            "CraftMetaBlockState",
            "CraftMetaItem",
            "GlowMetaItem"
    };

    private final XMaterial material;
    private int amount;

    private Message displayName;
    private Message lore;

    private int data;
    private final Map<XEnchantment, Integer> enchantments;
    private Map<String, NBT> nbt;

    private ItemMeta meta;

    public static NewBwItemStack valueOf(String str) {
        Matcher matcher = PATTERN.matcher(str);
        if (!matcher.matches()) return null;
        Optional<XMaterial> material = XMaterial.matchXMaterial(matcher.group("type"));
        if (!material.isPresent() || !material.get().isSupported()) return null;
        int amount = !NumberUtils.isDigits(matcher.group("amount")) ? 1 : Integer.parseInt(matcher.group("amount"));
        NewBwItemStack itemStack = new NewBwItemStack(material.get(), amount);
        if (NumberUtils.isDigits(matcher.group("data"))) {
            itemStack.setData(Short.parseShort(matcher.group("data")));
        }
        return itemStack;
    }

    public static NewBwItemStack fromJson(Json json) {
        Optional<XMaterial> optmaterial = XMaterial.matchXMaterial(json.get("type").getAsString());
        if (!optmaterial.isPresent()) {
            throw new IllegalArgumentException("Invalid material type: " + json.get("type").getAsString());
        }
        NewBwItemStack item = new NewBwItemStack(optmaterial.get());
        item.setAmount(json.has("amount") ? json.get("amount").getAsInt() : 1);
        item.setData(json.has("data") ? json.get("data").getAsShort() : 0);

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
        this.data = -1;
        this.nbt = new HashMap<>();
    }

    public NewBwItemStack(ItemStack itemStack) {
        this.material = XMaterial.matchXMaterial(itemStack);
        this.amount = itemStack.getAmount();
        this.meta = itemStack.getItemMeta().clone();
        this.data = itemStack.getDurability();
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
        this.nbt = DBedWarsAPI.getApi().getNMS().getNBTItem(itemStack).getTags();

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
        for (Map.Entry<XEnchantment, Integer> entry : this.enchantments.entrySet()) {
            if (!entry.getKey().isSupported()) continue;
            builder.withEnchantment(entry.getKey(), entry.getValue());
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
        for (Map.Entry<XEnchantment, Integer> entry : this.enchantments.entrySet()) {
            if (!entry.getKey().isSupported()) continue;
            builder.withEnchantment(entry.getKey(), entry.getValue());
        }
        ItemStack item = builder.toItemStack(this.amount);
        if (this.data > 0) {
            item.setDurability((short) this.data);
        }
        NBTItem nbtItem = DBedWarsAPI.getApi().getNMS().getNBTItem(item);
        nbtItem.applyTags(this.nbt);
        return nbtItem.getItem();
    }

}