package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTEnd extends NBT {

    public static final NBTEnd INSTANCE = new NBTEnd();

    private NBTEnd() {
    }

    @Override
    public NBTType<?> getType() {
        return NBTType.END;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return getClass() == obj.getClass();
    }

    @Override
    public String toString() {
        return "NBTEnd";
    }

    @Override
    public void read(DataInput input) throws IOException {

    }

    @Override
    public void write(DataOutput output) throws IOException {

    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public NBTEnd clone() {
        return this;
    }

}
