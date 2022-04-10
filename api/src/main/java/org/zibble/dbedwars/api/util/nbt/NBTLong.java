package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTLong extends NBTNumber {

    private long value;

    public NBTLong(long value) {
        this.value = value;
    }

    public NBTLong(DataInput input) throws IOException {
        super(input);
    }

    @Override
    public NBTType<?> getType() {
        return NBTType.LONG;
    }

    @Override
    public byte getAsByte() {
        return (byte) value;
    }

    @Override
    public short getAsShort() {
        return (short) value;
    }

    @Override
    public int getAsInt() {
        return (int) value;
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
        NBTLong other = (NBTLong) obj;
        return value == other.value;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.value = input.readLong();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeLong(this.value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public NBTLong clone() {
        return new NBTLong(this.value);
    }

}
