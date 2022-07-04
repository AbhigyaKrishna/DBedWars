package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTDouble extends NBTNumber {

    private double value;

    public NBTDouble(double value) {
        this.value = value;
    }

    public NBTDouble(DataInput input) throws IOException {
        super(input);
    }

    @Override
    public NBTType<?> getType() {
        return NBTType.DOUBLE;
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
        return (float) value;
    }

    @Override
    public double getAsDouble() {
        return value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
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
        NBTDouble other = (NBTDouble) obj;
        return Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
    }

    @Override
    public String toString() {
        return "NBTDouble= " + value;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.value = input.readDouble();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeDouble(this.value);
    }

    @Override
    public NBTDouble clone() {
        return new NBTDouble(this.value);
    }

}
