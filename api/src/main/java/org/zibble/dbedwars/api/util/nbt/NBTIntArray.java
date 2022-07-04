package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTIntArray extends NBT {

    private int[] array;

    public NBTIntArray(int[] array) {
        this.array = array;
    }

    public NBTIntArray(DataInput input) throws IOException {
        super(input);
    }

    @Override
    public NBTType<?> getType() {
        return NBTType.INT_ARRAY;
    }

    public int[] getValue() {
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
        NBTIntArray other = (NBTIntArray) obj;
        return Arrays.equals(array, other.array);
    }

    @Override
    public String toString() {
        return "NBTIntArray= " + Arrays.toString(array);
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.array = new int[input.readInt()];
        for (int i = 0; i < array.length; i++) {
            this.array[i] = input.readInt();
        }
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeInt(this.array.length);
        for (int i : array) {
            output.writeInt(i);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    @Override
    public NBTIntArray clone() {
        int[] aint = new int[this.array.length];
        System.arraycopy(this.array, 0, aint, 0, this.array.length);
        return new NBTIntArray(aint);
    }

}
