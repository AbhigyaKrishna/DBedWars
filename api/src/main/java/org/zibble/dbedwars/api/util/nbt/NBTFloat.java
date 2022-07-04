package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTFloat extends NBTNumber {

    private float value;

    public NBTFloat(float value) {
        this.value = value;
    }

    public NBTFloat(DataInput input) throws IOException {
        super(input);
    }

    @Override
    public NBTType<?> getType() {
        return NBTType.FLOAT;
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
        return (long) value;
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
        NBTFloat other = (NBTFloat) obj;
        return Float.floatToIntBits(value) == Float.floatToIntBits(other.value);
    }

    @Override
    public String toString() {
        return "NBTFloat=" + value;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.value = input.readFloat();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeFloat(this.value);
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    @Override
    public NBTFloat clone() {
        return new NBTFloat(this.value);
    }

}
