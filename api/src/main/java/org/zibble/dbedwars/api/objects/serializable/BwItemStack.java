package org.zibble.dbedwars.api.objects.serializable;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.LegacyMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.nms.NBTItem;
import org.zibble.dbedwars.api.objects.profile.PlayerGameProfile;
import org.zibble.dbedwars.api.objects.profile.Skin;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.api.util.NumberUtils;
import org.zibble.dbedwars.api.util.item.ItemMetaBuilder;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.api.util.nbt.NBTCompound;
import org.zibble.dbedwars.api.util.nbt.serializer.JsonNbtSerializer;
import org.zibble.inventoryframework.protocol.Item;
import org.zibble.inventoryframework.protocol.ProtocolPlayer;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BwItemStack implements Item, Cloneable {

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
    private NBTCompound nbtCompound;

    private ItemMeta meta;

    public static Builder builder() {
        return new Builder();
    }

    public BwItemStack(XMaterial material) {
        this(material, 1);
    }

    public BwItemStack(XMaterial material, int amount) {
        Preconditions.checkArgument(material.isSupported(), "Material " + material.name() + " is not supported");
        this.material = material;
        this.amount = amount;
        this.enchantments = new HashSet<>();
        this.data = -1;
        this.meta = material.parseItem().getItemMeta();
        this.nbtCompound = new NBTCompound();
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
            }
        }
        this.nbtCompound = new NBTCompound(DBedWarsAPI.getApi().getNMS().getNBTItem(itemStack).getTags());
    }

    public static BwItemStack valueOf(String str, Optional<Player> player, Placeholder... placeholders) {
        Messaging messaging = Messaging.get();
        Function<String, String> placeholderParser = player.<Function<String, String>>map(p -> s -> messaging.setRegisteredPlaceholders(messaging.setPlaceholders(s, p, placeholders), p))
                .orElseGet(() -> s -> messaging.setPlaceholders(s, placeholders));

        Matcher matcher = JSON_MATCHER.matcher(str);
        if (matcher.matches()) {
            String item = placeholderParser.apply(matcher.group("item"));

            BwItemStack configuredItem = DBedWarsAPI.getApi().getConfiguredItem(item, player, placeholders);
            String amt = matcher.group("amount");
            if (configuredItem != null && amt != null) {
                Integer num = NumberUtils.toInt(placeholderParser.apply(amt));
                if (num != null && num > 0) {
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
                itemStack.setAmount(NumberUtils.toInt(placeholderParser.apply(amt), 1));
            }
            String data = matcher.group("data");
            if (data != null) {
                itemStack.setData(NumberUtils.toShort(placeholderParser.apply(data), (short) 0));
            }
            return itemStack;
        }

        return null;
    }

    public static BwItemStack fromJson(Json json, Optional<Player> player, Placeholder... placeholders) {
        Messaging messaging = Messaging.get();
        Function<String, String> placeholderParser = player.<Function<String, String>>map(p -> s -> messaging.setRegisteredPlaceholders(messaging.setPlaceholders(s, p, placeholders), p))
                .orElseGet(() -> s -> messaging.setPlaceholders(s, placeholders));

        Optional<XMaterial> optmaterial = XMaterial.matchXMaterial(placeholderParser.apply(json.get("type").getAsString()));
        if (!optmaterial.isPresent()) {
            throw new IllegalArgumentException("Invalid material type: " + json.get("type").getAsString());
        }
        BwItemStack item = new BwItemStack(optmaterial.get());
        if (json.has("amount")) {
            item.setAmount(NumberUtils.toInt(placeholderParser.apply(json.get("amount").getAsString()), 1));
        }
        if (json.has("data")) {
            item.setData(NumberUtils.toInt(placeholderParser.apply(json.get("data").getAsString()), 0));
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
                LEnchant enchant = LEnchant.valueOf(placeholderParser.apply(element.getAsString()));
                if (enchant != null) {
                    item.addEnchantment(enchant);
                }
            }
        }
        if (json.has("flags")) {
            JsonArray array = json.getAsJsonArray("flags");
            for (JsonElement element : array) {
                ItemFlag flag = EnumUtil.matchEnum(placeholderParser.apply(element.getAsString()), ItemFlag.values());
                if (flag != null) {
                    item.getMeta().addItemFlags(flag);
                }
            }
        }
        if (json.has("nbt")) {
            Json array = json.getAsJson("nbt");
            for (Map.Entry<String, JsonElement> entry : array.entrySet()) {
                item.nbtCompound.setTag(entry.getKey(), JsonNbtSerializer.deserialize(entry.getValue()));
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
            SkullMeta skullMeta = (SkullMeta) item.getMeta();
            if (metaJson.has("owner")) {
                String owner = placeholderParser.apply(metaJson.get("owner").getAsString());
                try {
                    skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
                } catch (NoSuchMethodError e) {
                    skullMeta.setOwner(owner);
                }
            } else if (metaJson.has("texture")) {
                JsonObject texture = metaJson.getAsJsonObject("texture");
                Skin value = Skin.from(placeholderParser.apply(texture.get("value").getAsString()));
                if (texture.has("signature")) {
                    value.setSignature(placeholderParser.apply(texture.get("signature").getAsString()));
                }
                DBedWarsAPI.getApi().getNMS().setSkullProfile(skullMeta, PlayerGameProfile.builder().uuid(null).name("Head-" + UUID.randomUUID()).property(value).build());
            }
        } else if (item.getMeta() instanceof EnchantmentStorageMeta && metaJson.has("stored-enchants")) {
            EnchantmentStorageMeta storage = (EnchantmentStorageMeta) item.getMeta();
            JsonArray array = metaJson.getAsJsonArray("stored-enchants");
            for (JsonElement element : array) {
                LEnchant enchant = LEnchant.valueOf(placeholderParser.apply(element.getAsString()));
                if (enchant != null && enchant.getEnchantment().isSupported()) {
                    storage.addStoredEnchant(enchant.getEnchantment().getEnchant(), enchant.getLevel(), true);
                }
            }
        } else if (item.getMeta() instanceof LeatherArmorMeta) {
            if (metaJson.has("color")) {
                ((LeatherArmorMeta) item.getMeta()).setColor(org.bukkit.Color.fromRGB(Integer.parseInt(
                        placeholderParser.apply(metaJson.get("color").getAsString()), 16)));
            }
        } else if (item.getMeta() instanceof BookMeta) {
            BookMeta bookmeta = (BookMeta) item.getMeta();

            if (metaJson.has("title")) {
                bookmeta.setTitle(placeholderParser.apply(metaJson.get("title").getAsString()));
            }
            if (metaJson.has("author")) {
                bookmeta.setAuthor(placeholderParser.apply(metaJson.get("author").getAsString()));
            }
            if (metaJson.has("pages")) {
                JsonArray array = metaJson.getAsJsonArray("pages");
                List<String> pages = new ArrayList<>(array.size());
                for (JsonElement element : array) {
                    pages.add(placeholderParser.apply(element.getAsString()));
                }
                bookmeta.setPages(pages);
            }

        } else if (item.getMeta() instanceof PotionMeta) {
            PotionMeta potionmeta = (PotionMeta) item.getMeta();
            if (metaJson.has("potion-effects")) {
                JsonArray array = metaJson.getAsJsonArray("potion-effects");
                for (JsonElement element : array) {
                    PotionEffectAT effect = PotionEffectAT.valueOf(placeholderParser.apply(element.getAsString()));
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
                    FireworkEffectC effect = FireworkEffectC.valueOf(placeholderParser.apply(element.getAsString()));
                    if (effect != null) {
                        fireworkmeta.setEffect(effect.getEffect());
                    }
                }
            }
        } else if (item.getMeta() instanceof FireworkMeta) {
            FireworkMeta fireworkMeta = (FireworkMeta) item.getMeta();

            if (metaJson.has("power")) {
                fireworkMeta.setPower(NumberUtils.toInt(placeholderParser.apply(metaJson.get("power").getAsString())));
            }

            if (metaJson.has("firework-effect")) {
                JsonArray array = metaJson.getAsJsonArray("firework-effect");
                for (JsonElement element : array) {
                    FireworkEffectC effect = FireworkEffectC.valueOf(placeholderParser.apply(element.getAsString()));
                    if (effect != null) {
                        fireworkMeta.addEffect(effect.getEffect());
                    }
                }
            }
        } else if (item.getMeta() instanceof MapMeta) {
            MapMeta mapMeta = (MapMeta) item.getMeta();
            if (metaJson.has("location-name")) {
                mapMeta.setLocationName(placeholderParser.apply(metaJson.get("location-name").getAsString()));
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

    public NBTCompound getNbtCompound() {
        return this.nbtCompound;
    }

    public ItemStack asItemStack(Player player) {
        ItemMetaBuilder builder = ItemMetaBuilder.of(this.material, this.meta);
        if (this.displayName != null)
            builder.displayName(this.displayName.asComponentWithPAPI(player)[0]);
        if (this.lore != null)
            builder.lore(this.lore.asComponentWithPAPI(player));
        for (LEnchant enchant : this.enchantments) {
            if (!enchant.getEnchantment().isSupported()) continue;
            builder.withEnchantment(enchant.getEnchantment(), enchant.getLevel());
        }
        ItemStack item = builder.applyTo(this.material.parseItem());
        item.setAmount(this.amount);
        if (this.data > 0) {
            item.setDurability((short) this.data);
        }
        NBTItem nbtItem = DBedWarsAPI.getApi().getNMS().getNBTItem(item);
        nbtItem.applyTags(this.nbtCompound.getTags());
        return nbtItem.getItem();
    }

    public ItemStack asItemStack() {
        ItemMetaBuilder builder = ItemMetaBuilder.of(this.material, this.meta);
        if (this.displayName != null)
            builder.displayName(this.displayName.asComponent()[0]);
        if (this.lore != null)
            builder.lore(this.lore.asComponent());
        for (LEnchant enchant : this.enchantments) {
            if (!enchant.getEnchantment().isSupported()) continue;
            builder.withEnchantment(enchant.getEnchantment(), enchant.getLevel());
        }
        ItemStack item = builder.applyTo(this.material.parseItem());
        item.setAmount(this.amount);
        if (this.data > 0) {
            item.setDurability((short) this.data);
        }
        NBTItem nbtItem = DBedWarsAPI.getApi().getNMS().getNBTItem(item);
        nbtItem.applyTags(this.nbtCompound.getTags());
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
                && nbtCompound.equals(item.nbtCompound)
                && meta.equals(item.meta);
    }

    @Override
    public BwItemStack clone() {
        BwItemStack returnVal = new BwItemStack(this.material, this.amount);
        returnVal.displayName = this.displayName.clone();
        returnVal.lore = this.lore.clone();
        returnVal.meta = this.meta.clone();
        returnVal.data = this.data;
        returnVal.nbtCompound = this.nbtCompound.clone();
        this.enchantments.forEach(enchant -> returnVal.enchantments.add(enchant.clone()));
        return returnVal;
    }

    @Override
    public String toString() {
        return "BwItemStack{" +
                "material=" + material +
                ", amount=" + amount +
                ", displayName=" + displayName +
                ", lore=" + lore +
                ", data=" + data +
                ", enchantments=" + enchantments +
                ", nbtCompound=" + nbtCompound +
                ", meta=" + meta +
                '}';
    }

    @Override
    public com.github.retrooper.packetevents.protocol.item.@NotNull ItemStack asProtocol(ProtocolPlayer<?> protocolPlayer) {
        return DBedWarsAPI.getApi().getNMS().asPacketItem(this.asItemStack((Player) protocolPlayer.handle()));
    }

    public static class Builder implements org.zibble.dbedwars.api.util.mixin.Builder<BwItemStack> {

        private XMaterial material = XMaterial.AIR;
        private int amount = 1;
        private Message displayName;
        private Message lore;
        private final Set<LEnchant> enchantments = new HashSet<>();
        private int data = -1;
        private final NBTCompound compound = new NBTCompound();
        private ItemMeta meta = XMaterial.AIR.parseItem().getItemMeta();

        public Builder material(XMaterial material) {
            this.material = material;
            this.meta = material.parseItem().getItemMeta();
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder displayName(Message displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder lore(Message lore) {
            this.lore = lore;
            return this;
        }

        public Builder enchant(LEnchant enchant) {
            this.enchantments.add(enchant);
            return this;
        }

        public Builder data(int data) {
            this.data = data;
            return this;
        }

        public Builder nbt(Consumer<NBTCompound> consumer) {
            consumer.accept(this.compound);
            return this;
        }

        public Builder meta(Consumer<ItemMeta> consumer) {
            consumer.accept(this.meta);
            return this;
        }

        @Override
        public BwItemStack build() {
            BwItemStack item = new BwItemStack(this.material, this.amount);
            item.displayName = this.displayName;
            item.lore = this.lore;
            item.enchantments.addAll(this.enchantments);
            item.data = this.data;
            item.nbtCompound = this.compound;
            item.meta = this.meta;
            return item;
        }

    }

}