package org.zibble.dbedwars.database.data.io;

import com.google.common.collect.Multimap;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.database.data.PersistentStat;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

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

    public abstract <K, V> Map<K, V> readMap(String key, Function<String, K> keyMapper, Function<String, V> reader) throws Exception;

    public abstract <K, V> Multimap<K, V> readMultiMap(String key, Function<String, K> keyMapper, Function<String, Collection<V>> reader) throws Exception;

    public abstract <V> Collection<V> readList(String key, Function<String, V> valueMapper) throws Exception;

    public abstract <N extends Number> PersistentStat<N> readPersistentStat(String key, Class<N> type) throws Exception;

    public abstract Duration readDuration(String key) throws Exception;

    public abstract Instant readInstant(String key) throws Exception;

    public abstract <E extends Enum<E>> E readEnum(String key, Class<E> type) throws Exception;

}
