package org.zibble.dbedwars.api.util;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class NBTUtils {

    public static ItemStack addPluginData(ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        nbti.setString("plugin", "dbedwars");
        return nbti.getItem();
    }

    public static boolean hasPluginData(ItemStack item) {
        NBTItem nbti = new NBTItem(item);
        return nbti.hasNBTData()
                && nbti.hasKey("plugin")
                && nbti.getString("plugin").equals("dbedwars");
    }

    public static ItemStack addNbtData(ItemStack item, String key, Object value) {
        NBTItem nbti = new NBTItem(item);
        if (value instanceof String) {
            nbti.setString(key, (String) value);
        } else if (value instanceof Short) {
            nbti.setShort(key, (short) value);
        } else if (value instanceof Integer) {
            nbti.setInteger(key, (int) value);
        } else if (value instanceof Long) {
            nbti.setLong(key, (long) value);
        } else if (value instanceof Byte) {
            nbti.setByte(key, (byte) value);
        } else if (value instanceof Boolean) {
            nbti.setBoolean(key, (boolean) value);
        } else if (value instanceof Double) {
            nbti.setDouble(key, (double) value);
        } else if (value instanceof Float) {
            nbti.setFloat(key, (Float) value);
        } else if (value instanceof byte[]) {
            nbti.setByteArray(key, (byte[]) value);
        } else if (value instanceof int[]) {
            nbti.setIntArray(key, (int[]) value);
        } else if (value instanceof ItemStack) {
            nbti.setItemStack(key, (ItemStack) value);
        } else if (value instanceof UUID) {
            nbti.setUUID(key, (UUID) value);
        } else {
            nbti.setObject(key, value);
        }

        return nbti.getItem();
    }

    public static boolean hasNBTData(ItemStack item, String key) {
        NBTItem nbti = new NBTItem(item);
        return nbti.hasNBTData() && nbti.hasKey(key);
    }

    public static <T> T getValue(ItemStack item, String key, Class<T> clazz) {
        NBTItem nbti = new NBTItem(item);
        return nbti.getObject(key, clazz);
    }

    public static ItemStack removeNBTData(ItemStack item, String key) {
        if (!hasNBTData(item, key)) return item;

        NBTItem nbti = new NBTItem(item);
        nbti.removeKey(key);
        return nbti.getItem();
    }
}
