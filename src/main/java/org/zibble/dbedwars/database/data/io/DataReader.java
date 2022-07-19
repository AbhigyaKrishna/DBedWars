package org.zibble.dbedwars.database.data.io;

import com.google.common.collect.Multimap;
import org.zibble.dbedwars.api.objects.points.Count;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.util.DataType;
import org.zibble.dbedwars.api.util.key.Key;
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

    public abstract char readChar(Key key) throws Exception;

    public abstract String readString(Key key) throws Exception;

    public abstract boolean readBoolean(Key key) throws Exception;

    public abstract byte readByte(Key key) throws Exception;

    public abstract short readShort(Key key) throws Exception;

    public abstract int readInt(Key key) throws Exception;

    public abstract long readLong(Key key) throws Exception;

    public abstract float readFloat(Key key) throws Exception;

    public abstract double readDouble(Key key) throws Exception;

    public abstract <R extends Number> Count<R> readCount(Key key, DataType type) throws Exception;

    public abstract UUID readUUID(Key key) throws Exception;

    public abstract <K, V> Map<K, V> readMap(Key key, Function<String, K> keyMapper, Function<String, V> reader) throws Exception;

    public abstract <K, V> Multimap<K, V> readMultiMap(Key key, Function<String, K> keyMapper, Function<String, Collection<V>> reader) throws Exception;

    public abstract <V> Collection<V> readList(Key key, Function<String, V> valueMapper) throws Exception;

    public abstract <N extends Number> PersistentStat<N> readPersistentStat(Key key, Class<N> type) throws Exception;

    public abstract Duration readDuration(Key key) throws Exception;

    public abstract Instant readInstant(Key key) throws Exception;

    public abstract <E extends Enum<E>> E readEnum(Key key, Class<E> type) throws Exception;

}
