package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.IOException;

public abstract class NBTNumber extends NBT {

    public NBTNumber() {
    }

    public NBTNumber(DataInput input) throws IOException {
        super(input);
    }

    public abstract byte getAsByte();

    public abstract short getAsShort();

    public abstract int getAsInt();

    public abstract long getAsLong();

    public abstract float getAsFloat();

    public abstract double getAsDouble();

}
