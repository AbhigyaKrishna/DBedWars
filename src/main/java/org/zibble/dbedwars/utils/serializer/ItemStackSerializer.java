package org.zibble.dbedwars.utils.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.version.Version;
import org.zibble.dbedwars.utils.json.JSONBuilder;
import org.zibble.dbedwars.utils.json.Json;

import java.util.ArrayList;
import java.util.List;

public class ItemStackSerializer implements Serializer<ItemStack> {

    private static final String[] BYPASS_CLASS = {
            "CraftMetaBlockState",
            "CraftMetaItem",
            "GlowMetaItem"
    };

    @Override
    public Json serialize(@NotNull final ItemStack object) {

        final JSONBuilder builder = new JSONBuilder()
                .add("type", object.getType().name())
                .add("amount", object.getAmount())
                .add("durability", object.getDurability());

        if(!object.hasItemMeta())
            return builder.getRawJson();

        final ItemMeta meta = object.getItemMeta();
        final JsonObject metaJson = new JsonObject();


        if(meta.hasDisplayName())
            metaJson.addProperty("display-name", meta.getDisplayName());

        if(meta.hasLore()){
            final JsonArray loreArray = new JsonArray();
            meta.getLore().iterator().forEachRemaining(str -> loreArray.add(str));
            metaJson.add("lore",loreArray);
        }

        if(meta.hasEnchants()){
            final JsonArray enchantArray = new JsonArray();
            meta.getEnchants().forEach((enchantment, level) -> {
                enchantArray.add(enchantment.getName() +":"+ level);
            });
            metaJson.add("enchants",enchantArray);
        }

        if(!meta.getItemFlags().isEmpty()){
            final JsonArray flagArray = new JsonArray();
            meta.getItemFlags().iterator().forEachRemaining(itemFlag -> {
                flagArray.add(itemFlag.name());
            });
            metaJson.add("flags",flagArray);

        }


        for(String classNames : BYPASS_CLASS){
            if(meta.getClass().getSimpleName().equals(classNames)){
                builder.add("item-meta", metaJson);
                return builder.getRawJson();
            }
        }

        if(meta instanceof SkullMeta){
            final SkullMeta skullMeta = (SkullMeta) meta;
            if(skullMeta.hasOwner()){
                final JsonObject skullJson = new JsonObject();
                skullJson.addProperty("owner",skullMeta.getOwner());
                metaJson.add("extra-meta",skullJson);
            }
        }else if(meta instanceof EnchantmentStorageMeta){
            final EnchantmentStorageMeta enStMeta = (EnchantmentStorageMeta) meta;
            if(enStMeta.hasStoredEnchants()){
                final JsonObject enStJson = new JsonObject();
                final JsonArray storedEnchants = new JsonArray();

                enStMeta.getStoredEnchants().forEach((ench,level) -> {
                    storedEnchants.add(ench.getName() + ":" +level);
                });

                enStJson.add("stored-enchants",storedEnchants);
                metaJson.add("extra-meta",enStJson);
            }
        }else if(meta instanceof LeatherArmorMeta){
            final LeatherArmorMeta leMeta = (LeatherArmorMeta) meta;
            JsonObject extraMeta = new JsonObject();
            extraMeta.addProperty("color", Integer.toHexString(leMeta.getColor().asRGB()));
            metaJson.add("extra-meta",extraMeta);
        }else if(meta instanceof BookMeta){
            final BookMeta bMeta = (BookMeta) meta;

            if(bMeta.hasAuthor() || bMeta.hasPages() || bMeta.hasTitle()){
                JsonObject extraMeta = new JsonObject();
                if (bMeta.hasTitle()) {
                    extraMeta.addProperty("title", bMeta.getTitle());
                }
                if (bMeta.hasAuthor()) {
                    extraMeta.addProperty("author", bMeta.getAuthor());
                }
                if (bMeta.hasPages()) {
                    JsonArray pages = new JsonArray();
                    bMeta.getPages().forEach(str -> pages.add(str));
                    extraMeta.add("pages", pages);
                }
                metaJson.add("extra-meta",extraMeta);
            }

        }else if(meta instanceof PotionMeta){
            final PotionMeta pMeta = (PotionMeta) meta;
            JsonObject extraMeta = new JsonObject();
            if (pMeta.hasCustomEffects()) {
                JsonArray customEffects = new JsonArray();
                pMeta.getCustomEffects().forEach(potionEffect -> {
                    customEffects.add(potionEffect.getType().getName()
                            + ":" + potionEffect.getAmplifier()
                            + ":" + potionEffect.getDuration() / 20);
                });
                extraMeta.add("custom-effects", customEffects);
            } else {
                if (Version.SERVER_VERSION.isNewerEquals(Version.v1_9_R1)) {

                     PotionType type = pMeta.getBasePotionData().getType();
                     boolean isExtended = pMeta.getBasePotionData().isExtended();
                     boolean isUpgraded = pMeta.getBasePotionData().isUpgraded();
                     JsonObject baseEffect = new JsonObject();
                     baseEffect.addProperty("type", type.getEffectType().getName());
                     baseEffect.addProperty("isExtended", isExtended);
                     baseEffect.addProperty("isUpgraded", isUpgraded);
                     extraMeta.add("base-effect", baseEffect);
                }
            }
            metaJson.add("extra-meta",extraMeta);
        } else if(meta instanceof FireworkEffectMeta){
            final FireworkEffectMeta fMeta = (FireworkEffectMeta) meta;
            if (fMeta.hasEffect()) {
                FireworkEffect effect = fMeta.getEffect();
                JsonObject extraMeta = new JsonObject();

                extraMeta.addProperty("type", effect.getType().name());
                if (effect.hasFlicker()) {
                    extraMeta.addProperty("flicker", true);
                }
                if (effect.hasTrail()) {
                    extraMeta.addProperty("trail", true);
                }

                if (!effect.getColors().isEmpty()) {
                    JsonArray colors = new JsonArray();
                    effect.getColors().forEach(color ->
                            colors.add(Integer.toHexString(color.asRGB())));
                    extraMeta.add("colors", colors);
                }

                if (!effect.getFadeColors().isEmpty()) {
                    JsonArray fadeColors = new JsonArray();
                    effect.getFadeColors().forEach(color ->
                            fadeColors.add(Integer.toHexString(color.asRGB())));
                    extraMeta.add("fade-colors", fadeColors);
                }

                metaJson.add("extra-meta",extraMeta);
            }
        }else if(meta instanceof FireworkMeta){
            final FireworkMeta fMeta = (FireworkMeta) meta;
            JsonObject extraMeta = new JsonObject();

            extraMeta.addProperty("power", fMeta.getPower());

            if (fMeta.hasEffects()) {
                JsonArray effects = new JsonArray();
                fMeta.getEffects().forEach(effect -> {
                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("type", effect.getType().name());
                    if (effect.hasFlicker()) {
                        jsonObject.addProperty("flicker", true);
                    }
                    if (effect.hasTrail()) {
                        jsonObject.addProperty("trail", true);
                    }

                    if (!effect.getColors().isEmpty()) {
                        JsonArray colors = new JsonArray();
                        effect.getColors().forEach(color ->
                                colors.add(Integer.toHexString(color.asRGB())));
                        jsonObject.add("colors", colors);
                    }

                    if (!effect.getFadeColors().isEmpty()) {
                        JsonArray fadeColors = new JsonArray();
                        effect.getFadeColors().forEach(color ->
                                fadeColors.add(Integer.toHexString(color.asRGB())));
                        jsonObject.add("fade-colors", fadeColors);
                    }

                    effects.add(jsonObject);
                });
                extraMeta.add("effects", effects);
            }
            metaJson.add("extra-meta",extraMeta);
        }

        builder.add("item-meta",metaJson);
        return builder.getRawJson();
    }

    @Override
    public ItemStack deserialize(@NotNull Json json) {
        JsonElement element = JsonParser.parseString(json.toString());
        if (element.isJsonObject()) {
            JsonObject itemJson = element.getAsJsonObject();

            JsonElement typeElement = itemJson.get("type");
            JsonElement dataElement = itemJson.get("durability");
            JsonElement amountElement = itemJson.get("amount");

            if (typeElement.isJsonPrimitive()) {

                String type = typeElement.getAsString();
                short data = dataElement != null ? dataElement.getAsShort() : 0;
                int amount = amountElement != null ? amountElement.getAsInt() : 1;

                ItemStack itemStack = new ItemStack(Material.getMaterial(type));
                itemStack.setDurability(data);
                itemStack.setAmount(amount);

                JsonElement itemMetaElement = itemJson.get("item-meta");
                if (itemMetaElement != null && itemMetaElement.isJsonObject()) {

                    ItemMeta meta = itemStack.getItemMeta();
                    JsonObject metaJson = itemMetaElement.getAsJsonObject();

                    JsonElement displaynameElement = metaJson.get("display-name");
                    JsonElement loreElement = metaJson.get("lore");
                    JsonElement enchants = metaJson.get("enchants");
                    JsonElement flagsElement = metaJson.get("flags");
                    if (displaynameElement != null && displaynameElement.isJsonPrimitive()) {
                        meta.setDisplayName(displaynameElement.getAsString());
                    }
                    if (loreElement != null && loreElement.isJsonArray()) {
                        JsonArray jarray = loreElement.getAsJsonArray();
                        List<String> lore = new ArrayList<>(jarray.size());
                        jarray.forEach(jsonElement -> {
                            if (jsonElement.isJsonPrimitive()) {
                                lore.add(jsonElement.getAsString());
                            }
                        });
                        meta.setLore(lore);
                    }
                    if (enchants != null && enchants.isJsonArray()) {
                        JsonArray jarray = enchants.getAsJsonArray();
                        jarray.forEach(jsonElement -> {
                            if (jsonElement.isJsonPrimitive()) {
                                String enchantString = jsonElement.getAsString();
                                if (enchantString.contains(":")) {
                                    try {
                                        String[] splitEnchant = enchantString.split(":");
                                        Enchantment enchantment = Enchantment.getByName(splitEnchant[0]);
                                        int level = Integer.parseInt(splitEnchant[1]);
                                        if (enchantment != null && level > 0) {
                                            meta.addEnchant(enchantment, level, true);
                                        }
                                    } catch (NumberFormatException ignored) {
                                    }
                                }
                            }
                        });
                    }
                    if (flagsElement != null && flagsElement.isJsonArray()) {
                        JsonArray jarray = flagsElement.getAsJsonArray();
                        jarray.forEach(jsonElement -> {
                            if (jsonElement.isJsonPrimitive()) {
                                for (ItemFlag flag : ItemFlag.values()) {
                                    if (flag.name().equalsIgnoreCase(jsonElement.getAsString())) {
                                        meta.addItemFlags(flag);
                                        break;
                                    }
                                }
                            }
                        });
                    }
                    for (String clazz : BYPASS_CLASS) {
                        if (meta.getClass().getSimpleName().equals(clazz)) {
                            return itemStack;
                        }
                    }

                    JsonElement extrametaElement = metaJson.get("extra-meta");

                    if (extrametaElement != null
                            && extrametaElement.isJsonObject()) {
                        try {
                            JsonObject extraJson = extrametaElement.getAsJsonObject();
                            if (meta instanceof SkullMeta) {
                                JsonElement ownerElement = extraJson.get("owner");
                                if (ownerElement != null && ownerElement.isJsonPrimitive()) {
                                    SkullMeta smeta = (SkullMeta) meta;
                                    smeta.setOwner(ownerElement.getAsString());
                                }
                            } else if (meta instanceof EnchantmentStorageMeta) {
                                JsonElement storedEnchantsElement = extraJson.get("stored-enchants");
                                if (storedEnchantsElement != null && storedEnchantsElement.isJsonArray()) {
                                    EnchantmentStorageMeta esmeta = (EnchantmentStorageMeta) meta;
                                    JsonArray jarray = storedEnchantsElement.getAsJsonArray();
                                    jarray.forEach(jsonElement -> {
                                        if (jsonElement.isJsonPrimitive()) {
                                            String enchantString = jsonElement.getAsString();
                                            if (enchantString.contains(":")) {
                                                try {
                                                    String[] splitEnchant = enchantString.split(":");
                                                    Enchantment enchantment = Enchantment.getByName(splitEnchant[0]);
                                                    int level = Integer.parseInt(splitEnchant[1]);
                                                    if (enchantment != null && level > 0) {
                                                        esmeta.addStoredEnchant(enchantment, level, true);
                                                    }
                                                } catch (NumberFormatException ignored) {
                                                }
                                            }
                                        }
                                    });
                                }
                            } else if (meta instanceof LeatherArmorMeta) {
                                JsonElement colorElement = extraJson.get("color");
                                if (colorElement != null && colorElement.isJsonPrimitive()) {
                                    LeatherArmorMeta lameta = (LeatherArmorMeta) meta;
                                    try {
                                        lameta.setColor(Color.fromRGB(Integer.parseInt(colorElement.getAsString(),
                                                16)));
                                    } catch (NumberFormatException ignored) {
                                    }
                                }
                            } else if (meta instanceof BookMeta) {
                                final BookMeta  bmeta = (BookMeta) meta;
                                JsonElement titleElement = extraJson.get("title");
                                JsonElement authorElement = extraJson.get("author");
                                JsonElement pagesElement = extraJson.get("pages");

                                if (titleElement != null && titleElement.isJsonPrimitive()) {
                                    bmeta.setTitle(titleElement.getAsString());
                                }
                                if (authorElement != null && authorElement.isJsonPrimitive()) {
                                    bmeta.setAuthor(authorElement.getAsString());
                                }
                                if (pagesElement != null && pagesElement.isJsonArray()) {
                                    JsonArray jarray = pagesElement.getAsJsonArray();
                                    List<String> pages = new ArrayList<>(jarray.size());
                                    jarray.forEach(jsonElement -> {
                                        if (jsonElement.isJsonPrimitive()) {
                                            pages.add(jsonElement.getAsString());
                                        }
                                    });
                                    bmeta.setPages(pages);
                                }

                            } else if (meta instanceof PotionMeta) {
                                JsonElement customEffectsElement = extraJson.get("custom-effects");
                                PotionMeta pmeta = (PotionMeta) meta;
                                if (customEffectsElement != null && customEffectsElement.isJsonArray()) {
                                    JsonArray jarray = customEffectsElement.getAsJsonArray();
                                    jarray.forEach(jsonElement -> {
                                        if (jsonElement.isJsonPrimitive()) {
                                            String enchantString = jsonElement.getAsString();
                                            if (enchantString.contains(":")) {
                                                try {
                                                    String[] splitPotions = enchantString.split(":");
                                                    PotionEffectType potionType =
                                                            PotionEffectType.getByName(splitPotions[0]);
                                                    int amplifier = Integer.parseInt(splitPotions[1]);
                                                    int duration = Integer.parseInt(splitPotions[2]) * 20;
                                                    if (potionType != null) {
                                                        pmeta.addCustomEffect(new PotionEffect(potionType, amplifier,
                                                                duration), true);
                                                    }
                                                } catch (NumberFormatException ignored) {
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    if (Version.SERVER_VERSION.isNewerEquals(Version.v1_9_R1)) {
                                        JsonObject basePotion = extraJson.getAsJsonObject("base-effect");
                                        PotionType potionType = PotionType.valueOf(basePotion.get("type").getAsString());
                                        boolean isExtended = basePotion.get("isExtended").getAsBoolean();
                                        boolean isUpgraded = basePotion.get("isUpgraded").getAsBoolean();
                                        PotionData potionData = new PotionData(potionType, isExtended, isUpgraded);
                                        pmeta.setBasePotionData(potionData);
                                    }
                                }
                            } else if (meta instanceof FireworkEffectMeta) {
                                JsonElement effectTypeElement = extraJson.get("type");
                                JsonElement flickerElement = extraJson.get("flicker");
                                JsonElement trailElement = extraJson.get("trail");
                                JsonElement colorsElement = extraJson.get("colors");
                                JsonElement fadeColorsElement = extraJson.get("fade-colors");

                                if (effectTypeElement != null && effectTypeElement.isJsonPrimitive()) {
                                    FireworkEffectMeta femeta = (FireworkEffectMeta) meta;

                                    FireworkEffect.Type effectType =
                                            FireworkEffect.Type.valueOf(effectTypeElement.getAsString());

                                    if (effectType != null) {
                                        List<Color> colors = new ArrayList<>();
                                        if (colorsElement != null && colorsElement.isJsonArray()) {
                                            colorsElement.getAsJsonArray().forEach(colorElement -> {
                                                if (colorElement.isJsonPrimitive()) {
                                                    colors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                                }
                                            });
                                        }

                                        List<Color> fadeColors = new ArrayList<>();
                                        if (fadeColorsElement != null && fadeColorsElement.isJsonArray()) {
                                            fadeColorsElement.getAsJsonArray().forEach(colorElement -> {
                                                if (colorElement.isJsonPrimitive()) {
                                                    fadeColors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                                }
                                            });
                                        }

                                        FireworkEffect.Builder builder = FireworkEffect.builder().with(effectType);

                                        if (flickerElement != null && flickerElement.isJsonPrimitive()) {
                                            builder.flicker(flickerElement.getAsBoolean());
                                        }
                                        if (trailElement != null && trailElement.isJsonPrimitive()) {
                                            builder.trail(trailElement.getAsBoolean());
                                        }

                                        if (!colors.isEmpty()) {
                                            builder.withColor(colors);
                                        }
                                        if (!fadeColors.isEmpty()) {
                                            builder.withFade(fadeColors);
                                        }

                                        femeta.setEffect(builder.build());
                                    }
                                }
                            } else if (meta instanceof FireworkMeta) {
                                FireworkMeta fmeta = (FireworkMeta) meta;

                                JsonElement effectArrayElement = extraJson.get("effects");
                                JsonElement powerElement = extraJson.get("power");

                                if (powerElement != null && powerElement.isJsonPrimitive()) {
                                    fmeta.setPower(powerElement.getAsInt());
                                }

                                if (effectArrayElement != null && effectArrayElement.isJsonArray()) {

                                    effectArrayElement.getAsJsonArray().forEach(jsonElement -> {
                                        if (jsonElement.isJsonObject()) {

                                            JsonObject jsonObject = jsonElement.getAsJsonObject();

                                            JsonElement effectTypeElement = jsonObject.get("type");
                                            JsonElement flickerElement = jsonObject.get("flicker");
                                            JsonElement trailElement = jsonObject.get("trail");
                                            JsonElement colorsElement = jsonObject.get("colors");
                                            JsonElement fadeColorsElement = jsonObject.get("fade-colors");

                                            if (effectTypeElement != null && effectTypeElement.isJsonPrimitive()) {

                                                FireworkEffect.Type effectType =
                                                        FireworkEffect.Type.valueOf(effectTypeElement.getAsString());

                                                if (effectType != null) {
                                                    List<Color> colors = new ArrayList<>();
                                                    if (colorsElement != null && colorsElement.isJsonArray()) {
                                                        colorsElement.getAsJsonArray().forEach(colorElement -> {
                                                            if (colorElement.isJsonPrimitive()) {
                                                                colors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                                            }
                                                        });
                                                    }

                                                    List<Color> fadeColors = new ArrayList<>();
                                                    if (fadeColorsElement != null && fadeColorsElement.isJsonArray()) {
                                                        fadeColorsElement.getAsJsonArray().forEach(colorElement -> {
                                                            if (colorElement.isJsonPrimitive()) {
                                                                fadeColors.add(Color.fromRGB(Integer.parseInt(colorElement.getAsString(), 16)));
                                                            }
                                                        });
                                                    }

                                                    FireworkEffect.Builder builder =
                                                            FireworkEffect.builder().with(effectType);

                                                    if (flickerElement != null && flickerElement.isJsonPrimitive()) {
                                                        builder.flicker(flickerElement.getAsBoolean());
                                                    }
                                                    if (trailElement != null && trailElement.isJsonPrimitive()) {
                                                        builder.trail(trailElement.getAsBoolean());
                                                    }

                                                    if (!colors.isEmpty()) {
                                                        builder.withColor(colors);
                                                    }
                                                    if (!fadeColors.isEmpty()) {
                                                        builder.withFade(fadeColors);
                                                    }

                                                    fmeta.addEffect(builder.build());
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            Bukkit.getLogger().warning("Failed to set extra meta for the item-stack");
                            e.printStackTrace();
                            return null;
                        }
                    }
                    itemStack.setItemMeta(meta);
                }
                return itemStack;
            } else {
                Bukkit.getLogger().warning("Failed to identify the provided as a json primitive for the item-stack");
                return null;
            }
        } else {
            Bukkit.getLogger().warning("Failed to identify the provided as a json object for the item-stack");
            return null;
        }
    }

    @Override
    public Class<ItemStack> clazz() {
        return ItemStack.class;
    }
}

