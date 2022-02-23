package org.zibble.dbedwars.utils.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.utils.json.Json;

public class ItemStackSerializer implements Serializer<ItemStack> {

    private static final String[] BYPASS_CLASS = {
            "CraftMetaBlockState",
            "CraftMetaItem",
            "GlowMetaItem"
    };

    @Override
    public String serialize(@NotNull final ItemStack object) {

        final Gson gson = new Gson();

        final JsonObject stackJson = new JsonObject();
        stackJson.addProperty("type", object.getType().name());
        stackJson.addProperty("amount", object.getAmount());
        stackJson.addProperty("durability", object.getDurability());

        if(!object.hasItemMeta())
            return gson.toJson(stackJson);

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
                stackJson.add("item-meta", metaJson);
                return stackJson.toString();
            }
        }

        return null;
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

