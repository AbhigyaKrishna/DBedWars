package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTByteArray extends NBT {

    private byte[] array;

    public NBTByteArray(byte[] array) {
        this.array = array;
    }

    public NBTByteArray(DataInput input) throws IOException {
        super(input);
    }

    @Override
    public NBTType<?> getType() {
        return NBTType.BYTE_ARRAY;
    }

    public byte[] getValue() {
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
        NBTByteArray other = (NBTByteArray) obj;
        return Arrays.equals(array, other.array);
    }

    @Override
    public String toString() {
        return "NBTByteArray= " + Arrays.toString(array);
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.array = new byte[input.readInt()];
        input.readFully(this.array);
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeInt(this.array.length);
        output.write(this.array);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }

    @Override
    public NBTByteArray clone() {
        byte[] abyte = new byte[this.array.length];
        System.arraycopy(this.array, 0, abyte, 0, this.array.length);
        return new NBTByteArray(abyte);
    }

}
