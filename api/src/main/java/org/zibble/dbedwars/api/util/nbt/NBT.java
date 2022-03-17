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

    public abstract NBTType getType();

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

    protected NBT readTag(DataInput input, NBTType type) throws IOException {
        switch (type) {
            case END:
                return NBTEnd.INSTANCE;
            case BYTE:
                return new NBTByte(input);
            case SHORT:
                return new NBTShort(input);
            case INT:
                return new NBTInt(input);
            case LONG:
                return new NBTLong(input);
            case FLOAT:
                return new NBTFloat(input);
            case DOUBLE:
                return new NBTDouble(input);
            case BYTE_ARRAY:
                return new NBTByteArray(input);
            case STRING:
                return new NBTString(input);
            case LIST:
                return new NBTList<>(input);
            case COMPOUND:
                return new NBTCompound(input);
            case INT_ARRAY:
                return new NBTIntArray(input);
            case LONG_ARRAY:
                return new NBTLongArray(input);
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
