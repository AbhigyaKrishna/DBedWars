package org.zibble.dbedwars.api.util.nbt;

import com.google.common.base.Objects;

public class NBTType<T extends NBT> {

    public static final NBTType<NBTEnd> END = new NBTType<>(NBTEnd.class, 0);
    public static final NBTType<NBTByte> BYTE = new NBTType<>(NBTByte.class, 1);
    public static final NBTType<NBTShort> SHORT = new NBTType<>(NBTShort.class, 2);
    public static final NBTType<NBTInt> INT = new NBTType<>(NBTInt.class, 3);
    public static final NBTType<NBTLong> LONG = new NBTType<>(NBTLong.class, 4);
    public static final NBTType<NBTFloat> FLOAT = new NBTType<>(NBTFloat.class, 5);
    public static final NBTType<NBTDouble> DOUBLE = new NBTType<>(NBTDouble.class, 6);
    public static final NBTType<NBTByteArray> BYTE_ARRAY = new NBTType<>(NBTByteArray.class, 7);
    public static final NBTType<NBTString> STRING = new NBTType<>(NBTString.class, 8);
    public static final NBTType<NBTList> LIST = new NBTType<>(NBTList.class, 9);
    public static final NBTType<NBTCompound> COMPOUND = new NBTType<>(NBTCompound.class, 10);
    public static final NBTType<NBTIntArray> INT_ARRAY = new NBTType<>(NBTIntArray.class, 11);
    public static final NBTType<NBTLongArray> LONG_ARRAY = new NBTType<>(NBTLongArray.class, 12);

    public static final NBTType<?>[] VALUES = new NBTType[]{END, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BYTE_ARRAY, STRING, LIST, COMPOUND, INT_ARRAY, LONG_ARRAY};

    private final Class<T> clazz;
    private final int id;

    public NBTType(Class<T> clazz, int id) {
        this.clazz = clazz;
        this.id = id;
    }

    public static NBTType<?> fromId(int id) {
        if (id < 0 || id >= VALUES.length) {
            return null;
        }
        return VALUES[id];
    }

    public int getId() {
        return this.id;
    }

    public Class<?> getNBTClass() {
        return this.clazz;
    }

    @Override
    public boolean equals(Object value) {
        if (this == value) return true;
        if (!(value instanceof NBTType)) return false;
        NBTType<?> type = (NBTType<?>) value;
        return id == type.id && Objects.equal(clazz, type.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clazz, id);
    }

    @Override
    public String toString() {
        return this.clazz.getSimpleName();
    }

}
