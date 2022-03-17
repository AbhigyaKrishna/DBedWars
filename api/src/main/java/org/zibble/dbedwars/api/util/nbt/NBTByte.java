package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTByte extends NBTNumber {

    private byte value;

    public NBTByte(byte value) {
        this.value = value;
    }

    public NBTByte(boolean value) {
        this((byte) (value ? 1 : 0));
    }

    public NBTByte(DataInput input) throws IOException {
        super(input);
    }

    @Override
    public NBTType getType() {
        return NBTType.BYTE;
    }

    @Override
    public byte getAsByte() {
        return value;
    }

    @Override
    public short getAsShort() {
        return value;
    }

    @Override
    public int getAsInt() {
        return value;
    }

    @Override
    public long getAsLong() {
        return value;
    }

    @Override
    public float getAsFloat() {
        return value;
    }

    @Override
    public double getAsDouble() {
        return value;
    }

    @Override
    public int hashCode() {
        return Byte.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NBTByte other = (NBTByte) obj;
        return value == other.value;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.value = input.readByte();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeByte(value);
    }

    @Override
    public NBTByte clone() {
        return new NBTByte(this.value);
    }
}
