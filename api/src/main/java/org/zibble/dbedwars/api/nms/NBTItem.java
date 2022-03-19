package org.zibble.dbedwars.api.nms;

import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.util.nbt.NBT;
import org.zibble.dbedwars.api.util.nbt.NBTCompound;
import org.zibble.dbedwars.api.util.nbt.NBTList;
import org.zibble.dbedwars.api.util.nbt.NBTType;

import java.util.Map;

public interface NBTItem {

    ItemStack getItem();

    void applyNbt(String key, NBT nbt);

    void applyTags(Map<String, ? extends NBT> nbts);

    Map<String, NBT> getTags();

    NBT getTag(String key);

    boolean hasKey(String key);

    void setString(String key, String value);

    void setInt(String key, int value);

    void setDouble(String key, double value);

    void setBoolean(String key, boolean value);

    void setByte(String key, byte value);

    void setShort(String key, short value);

    void setLong(String key, long value);

    void setFloat(String key, float value);

    void setByteArray(String key, byte[] value);

    void setIntArray(String key, int[] value);

    void setLongArray(String key, long[] value);

    void setList(String key, NBTList<? extends NBT> value);

    void setCompound(String key, NBTCompound value);

    interface INbt {

        Object getHandle();

        NBTType<?> getType();

        NBT asNBT();

    }

    interface INbtCompound extends INbt {

        boolean hasKey(String key);

        Map<String, ? extends INbt> getTags();

        INbt getTag(String key);

        void setTag(String key, NBT value);

        void setTag(String key, INbt value);

        void setString(String key, String value);

        void setInt(String key, int value);

        void setDouble(String key, double value);

        void setBoolean(String key, boolean value);

        void setByte(String key, byte value);

        void setShort(String key, short value);

        void setLong(String key, long value);

        void setFloat(String key, float value);

        void setByteArray(String key, byte[] value);

        void setIntArray(String key, int[] value);

        void setLongArray(String key, long[] value);

        void setList(String key, NBTList<? extends NBT> value);

        void setCompound(String key, NBTCompound value);

    }

}
