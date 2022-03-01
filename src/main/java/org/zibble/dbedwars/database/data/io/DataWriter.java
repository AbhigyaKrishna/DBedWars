package org.zibble.dbedwars.database.data.io;

import java.util.UUID;

public abstract class DataWriter <T>{

    protected T data;

    public DataWriter(T data){
        this.data = data;
    }

    public T getData(){
        return data;
    }

    public abstract void writeChar(String key, char value);

    public abstract void writeString(String key, String value);

    public abstract void writeBoolean(String key, boolean value);

    public abstract void writeByte(String key, byte value);

    public abstract void writeInt(String key, int value);

    public abstract void writeLong(String key, long value);

    public abstract void writeShort(String key, short value);

    public abstract void writeFloat(String key, float value);

    public abstract void writeDouble(String key, double value);

    public abstract void writeUUID(String key, UUID value);

}
