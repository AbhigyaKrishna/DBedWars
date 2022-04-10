package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTInt extends NBTNumber {

    private int value;

    public NBTInt(int value) {
        this.value = value;
    }

    public NBTInt(DataInput input) throws IOException {
        super(input);
    }

    @Override
    public NBTType<?> getType() {
        return NBTType.INT;
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
        NBTInt other = (NBTInt) obj;
        return value == other.value;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.value = input.readInt();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeInt(this.value);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public NBTInt clone() {
        return new NBTInt(this.value);
    }

}
