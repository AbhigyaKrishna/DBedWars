package org.zibble.dbedwars.database.data.io;

import com.google.common.collect.Multimap;
import org.zibble.dbedwars.api.objects.points.Count;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.database.data.PersistentStat;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class DataWriter<T> {

    protected T data;

    public DataWriter(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public abstract void writeChar(Key key, char value) throws Exception;

    public abstract void writeString(Key key, String value) throws Exception;

    public abstract void writeBoolean(Key key, boolean value) throws Exception;

    public abstract void writeByte(Key key, byte value) throws Exception;

    public abstract void writeInt(Key key, int value) throws Exception;

    public abstract void writeLong(Key key, long value) throws Exception;

    public abstract void writeShort(Key key, short value) throws Exception;

    public abstract void writeFloat(Key key, float value) throws Exception;

    public abstract void writeDouble(Key key, double value) throws Exception;

    public abstract <R extends Number> void writeCount(Key key, Count<R> value) throws Exception;

    public abstract void writeUUID(Key key, UUID value) throws Exception;

    public abstract <K, V> void writeMap(Key key, Map<K, V> value, Function<K, String> keyMapper, BiConsumer<String, V> valueWriter) throws Exception;

    public abstract <K, V> void writeMultiMap(Key key, Multimap<K, V> value, Function<K, String> keyMapper, BiConsumer<String, Collection<V>> valueWriter) throws Exception;

    public abstract <V> void writeList(Key key, Collection<V> value, Function<V, String> valueMapper) throws Exception;

    public abstract void writePersistentStat(Key key, PersistentStat<? extends Number> value) throws Exception;

    public abstract void writeDuration(Key key, Duration value) throws Exception;

    public abstract void writeInstant(Key key, Instant value) throws Exception;

    public abstract <E extends Enum<E>> void writeEnum(Key key, E value) throws Exception;

}
