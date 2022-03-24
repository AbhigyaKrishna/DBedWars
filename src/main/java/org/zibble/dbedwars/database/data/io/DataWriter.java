package org.zibble.dbedwars.database.data.io;

import com.google.common.collect.Multimap;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.database.data.PersistentStat;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class DataWriter <T> {

    protected T data;

    public DataWriter(T data){
        this.data = data;
    }

    public T getData(){
        return data;
    }

    public abstract void writeChar(String key, char value) throws Exception;

    public abstract void writeString(String key, String value) throws Exception;

    public abstract void writeBoolean(String key, boolean value) throws Exception;

    public abstract void writeByte(String key, byte value) throws Exception;

    public abstract void writeInt(String key, int value) throws Exception;

    public abstract void writeLong(String key, long value) throws Exception;

    public abstract void writeShort(String key, short value) throws Exception;

    public abstract void writeFloat(String key, float value) throws Exception;

    public abstract void writeDouble(String key, double value) throws Exception;

    public abstract void writeUUID(String key, UUID value) throws Exception;

    public abstract <K, V> void writeMap(String key, Map<K, V> value, Function<K, String> keyMapper, BiConsumer<String, V> valueWriter) throws Exception;

    public abstract <K, V> void writeMultiMap(String key, Multimap<K, V> value, Function<K, String> keyMapper, BiConsumer<String, Collection<V>> valueWriter) throws Exception;

    public abstract <V> void writeList(String key, Collection<V> value, Function<V, String> valueMapper) throws Exception;

    public abstract void writePersistentStat(String key, PersistentStat<? extends Number> value) throws Exception;

    public abstract void writeDuration(String key, Duration value) throws Exception;

    public abstract void writeInstant(String key, Instant value) throws Exception;

    public abstract <E extends Enum<E>> void writeEnum(String key, E value) throws Exception;

}
