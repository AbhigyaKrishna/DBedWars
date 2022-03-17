package org.zibble.dbedwars.api.util.nbt;

public enum NBTType {

    END(NBTEnd.class),
    BYTE(NBTByte.class),
    SHORT(NBTShort.class),
    INT(NBTInt.class),
    LONG(NBTLong.class),
    FLOAT(NBTFloat.class),
    DOUBLE(NBTDouble.class),
    BYTE_ARRAY(NBTByteArray.class),
    STRING(NBTString.class),
    LIST(NBTList.class),
    COMPOUND(NBTCompound.class),
    INT_ARRAY(NBTIntArray.class),
    LONG_ARRAY(NBTLongArray.class),
    ;

    public static final NBTType[] VALUES = values();

    private final Class<?> clazz;

    NBTType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public int getId() {
        return this.ordinal();
    }

    public Class<?> getNBTClass() {
        return this.clazz;
    }

    @Override
    public String toString() {
        return this.clazz.getSimpleName();
    }
    
    public static NBTType fromId(int id) {
        if (id < 0 || id >= values().length) {
            return null;
        }
        return VALUES[id];
    }

}
