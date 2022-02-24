package org.zibble.dbedwars.utils.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.utils.json.JSONBuilder;
import org.zibble.dbedwars.utils.json.Json;

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
                /**
                PotionType type = pMeta.getBasePotionData().getType();
                boolean isExtended = pMeta.getBasePotionData().isExtended();
                boolean isUpgraded = pMeta.getBasePotionData().isUpgraded();
                JsonObject baseEffect = new JsonObject();
                baseEffect.addProperty("type", type.getEffectType().getName());
                baseEffect.addProperty("isExtended", isExtended);
                baseEffect.addProperty("isUpgraded", isUpgraded);
                extraMeta.add("base-effect", baseEffect);
                 **/
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



        return null;
    }

    @Override
    public Class<ItemStack> clazz() {
        return ItemStack.class;
    }
}

