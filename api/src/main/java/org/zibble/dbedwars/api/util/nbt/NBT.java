package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.MessageFormat;

public abstract class NBT implements Cloneable {

    public NBT() {
    }

    public NBT(DataInput input) throws IOException {
        this.read(input);
    }

    public abstract NBTType<?> getType();

    @Override
    public abstract boolean equals(Object other);

    public abstract void read(DataInput input) throws IOException;

    public abstract void write(DataOutput output) throws IOException;

    @Override
    public abstract int hashCode();

    @Override
    public String toString() {
        return "nbt";
    }

    public abstract NBT clone();

    protected NBTType readTagType(DataInput input) throws IOException {
        int id = input.readByte();
        NBTType type = NBTType.fromId(id);
        if (type == null) {
            throw new IOException(MessageFormat.format("Unknown nbt type id {0}", id));
        }
        return type;
    }

    protected String readTagName(DataInput input) throws IOException {
        return input.readUTF();
    }

    protected <T extends NBT> T readTag(DataInput input, NBTType<T> type) throws IOException {
        if (type.equals(NBTType.END)) {
            return (T) NBTEnd.INSTANCE;
        } else if (type.equals(NBTType.BYTE)) {
            return (T) new NBTByte(input);
        } else if (type.equals(NBTType.SHORT)) {
            return (T) new NBTShort(input);
        } else if (type.equals(NBTType.INT)) {
            return (T) new NBTInt(input);
        } else if (type.equals(NBTType.LONG)) {
            return (T) new NBTLong(input);
        } else if (type.equals(NBTType.FLOAT)) {
            return (T) new NBTFloat(input);
        } else if (type.equals(NBTType.DOUBLE)) {
            return (T) new NBTDouble(input);
        } else if (type.equals(NBTType.BYTE_ARRAY)) {
            return (T) new NBTByteArray(input);
        } else if (type.equals(NBTType.STRING)) {
            return (T) new NBTString(input);
        } else if (type.equals(NBTType.LIST)) {
            return (T) new NBTList(input);
        } else if (type.equals(NBTType.COMPOUND)) {
            return (T) new NBTCompound(input);
        } else if (type.equals(NBTType.INT_ARRAY)) {
            return (T) new NBTIntArray(input);
        } else if (type.equals(NBTType.LONG_ARRAY)) {
            return (T) new NBTLongArray(input);
        }
        throw new IOException(MessageFormat.format("Unknown nbt type {0}", type));
    }

    protected void writeTagType(DataOutput output, NBTType type) throws IOException {
        output.writeByte(type.getId());
    }

    protected void writeTagName(DataOutput output, String name) throws IOException {
        output.writeUTF(name);
    }

    protected void writeTag(DataOutput output, NBT tag) throws IOException {
        tag.write(output);
    }

}
