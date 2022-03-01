package org.zibble.dbedwars.database.data.io;

import java.util.UUID;

public abstract class DataReader<T> {

    protected final T handle;

    protected DataReader(T handle) {
        this.handle = handle;
    }

    public T getHandle() {
        return handle;
    }

    public abstract char readChar(String key) throws Exception;

    public abstract String readString(String key) throws Exception;

    public abstract boolean readBoolean(String key) throws Exception;

    public abstract byte readByte(String key) throws Exception;

    public abstract short readShort(String key) throws Exception;

    public abstract int readInt(String key) throws Exception;

    public abstract long readLong(String key) throws Exception;

    public abstract float readFloat(String key) throws Exception;

    public abstract double readDouble(String key) throws Exception;

    public abstract UUID readUUID(String key) throws Exception;

}
