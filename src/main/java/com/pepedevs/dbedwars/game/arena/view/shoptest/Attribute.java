package com.pepedevs.dbedwars.game.arena.view.shoptest;

import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.radium.utils.configuration.annotations.LoadableEntry;
import com.pepedevs.radium.utils.itemstack.ItemMetaBuilder;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.view.AttributeType;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.LEnchant;
import com.pepedevs.dbedwars.api.util.item.BedWarsActionItem;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableShop;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Attribute implements com.pepedevs.dbedwars.api.game.view.Attribute {

    private final AttributeType type;
    private final Map<String, Object> keyEntry;

    public Attribute(AttributeType type) {
        this.type = type;
        this.keyEntry = new HashMap<>();
    }

    protected void loadFromConfig(ConfigurableShop.ConfigurableAttribute atr) {
        if (type.getKeys() != null) {
            for (String key : type.getKeys()) {
                for (Field field : atr.getClass().getDeclaredFields()) {
                    if (!field.isAnnotationPresent(LoadableEntry.class)) continue;

                    LoadableEntry entry = field.getAnnotation(LoadableEntry.class);
                    if (entry.key() != null && entry.key().equals(key)) {
                        try {
                            field.setAccessible(true);
                            Object value = field.get(atr);
                            this.keyEntry.put(key, value);
                        } catch (IllegalAccessException ignored) {
                        }
                    }
                }
            }
        }

        if (type == AttributeType.PURCHASABLE) {
            for (Map.Entry<String, ConfigurableShop.ConfigurableAttribute.AttributeItems> atrItem :
                    atr.getItemsToGive().entrySet()) {
                if (atrItem.getValue().isInvalid()) continue;
                if (atrItem.getValue().getMaterial() != null) {
                    boolean isTeamColor = atrItem.getValue().getMaterial().contains("%team%");
                    BwItemStack stack =
                            ConfigurationUtils.parseItem(null, atrItem.getValue().getMaterial());
                    stack.setAmount(atrItem.getValue().getAmount());
                    ItemMetaBuilder builder = stack.getItemMetaBuilder();
                    if (atrItem.getValue().getName() != null)
                        builder.withDisplayName(
                                StringUtils.translateAlternateColorCodes(
                                        atrItem.getValue().getName()));
                    if (!atrItem.getValue().getLore().isEmpty())
                        builder.withLore(
                                StringUtils.translateAlternateColorCodes(
                                        atrItem.getValue().getLore()));
                    stack.setItemMetaBuilder(builder);
                    if (atrItem.getValue().getEnchant() != null)
                        atrItem.getValue().getEnchant().stream()
                                .map(LEnchant::valueOf)
                                .filter(Objects::nonNull)
                                .forEach(stack::applyEnchant);
                    this.keyEntry.put("item-" + isTeamColor + "-" + atrItem.getKey(), stack);
                } else if (atrItem.getValue().getCustomItem() != null) {
                    try {
                        BedWarsActionItem item =
                                DBedwars.getInstance()
                                        .getCustomItemHandler()
                                        .getItem(atrItem.getValue().getCustomItem());
                        if (item == null) continue;
                        this.keyEntry.put("item-" + atrItem.getKey(), item.toBwItemStack());
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        } else if (type == AttributeType.AUTO_EQUIP) {
            for (Map.Entry<String, ConfigurableShop.ConfigurableAttribute.AttributeItems> atrItem :
                    atr.getItemsToGive().entrySet()) {
                this.keyEntry.put(
                        AttributeType.AUTO_EQUIP.getKeys()[0] + "-" + atrItem.getKey(),
                        atrItem.getValue().getAutoEquipSlot());
            }
        }
    }

    @Override
    public AttributeType getType() {
        return this.type;
    }

    @Override
    public Map<String, Object> getKeyEntry() {
        return this.keyEntry;
    }
}
