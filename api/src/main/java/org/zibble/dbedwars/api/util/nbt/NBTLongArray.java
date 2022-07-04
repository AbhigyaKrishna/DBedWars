package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTLongArray extends NBT {

    private long[] array;

    public NBTLongArray(long[] array) {
        this.array = array;
    }

    public NBTLongArray(DataInput input) throws IOException {
        super(input);
    }

    @Override
    public NBTType<?> getType() {
        return NBTType.LONG_ARRAY;
    }

    public long[] getValue() {
        return array;
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
        NBTLongArray other = (NBTLongArray) obj;
        return Arrays.equals(array, other.array);
    }

    @Override
    public String toString() {
        return "NBTLongArray= " + Arrays.toString(array);
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.array = new long[input.readInt()];
        for (int i = 0; i < array.length; i++) {
            this.array[i] = input.readLong();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeInt(array.length);
        for (long l : array) {
            output.writeLong(l);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    @Override
    public NBTLongArray clone() {
        long[] along = new long[this.array.length];
        System.arraycopy(this.array, 0, along, 0, this.array.length);
        return new NBTLongArray(along);
    }

}
