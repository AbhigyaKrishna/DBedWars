package org.zibble.dbedwars.api.util.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class NBTString extends NBT {

    private String value;

    public NBTString(String value) {
        this.value = value;
    }

    public NBTString(DataInput input) throws IOException {
        super(input);
    }

    @Override
    public NBTType<?> getType() {
        return NBTType.STRING;
    }

    public String getValue() {
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
        NBTString other = (NBTString) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return "NBTString= " + value;
    }

    @Override
    public void read(DataInput input) throws IOException {
        this.value = input.readUTF();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(this.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public NBTString clone() {
        return new NBTString(this.value);
    }

}
