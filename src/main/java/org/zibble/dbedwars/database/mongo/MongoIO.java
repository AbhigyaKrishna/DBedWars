package org.zibble.dbedwars.database.mongo;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.database.data.PersistentStat;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

import java.time.Instant;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MongoIO {

    public static class MongoDataWriter extends DataWriter<BsonWriter> {

        private final CodecRegistry codecRegistry;
        private final EncoderContext encoderContext;

        public MongoDataWriter(BsonWriter data, CodecRegistry registries, EncoderContext encoderContext) {
            super(data);
            this.codecRegistry = registries;
            this.encoderContext = encoderContext;
        }

        @Override
        public void writeChar(String key, char value) {
            this.data.writeName(key);
            this.data.writeInt32(Character.getNumericValue(value));
        }

        @Override
        public void writeString(String key, String value) {
            this.data.writeName(key);
            this.data.writeString(value);
        }

        @Override
        public void writeBoolean(String key, boolean value) {
            this.data.writeName(key);
            this.data.writeBoolean(value);
        }

        @Override
        public void writeByte(String key, byte value) {
            this.data.writeName(key);
            this.data.writeInt32(Byte.toUnsignedInt(value));
        }

        @Override
        public void writeInt(String key, int value) {
            this.data.writeName(key);
            this.data.writeInt32(value);
        }

        @Override
        public void writeLong(String key, long value) {
            this.data.writeName(key);
            this.data.writeInt64(value);
        }

        @Override
        public void writeShort(String key, short value) {
            this.data.writeName(key);
            this.data.writeInt32(Short.toUnsignedInt(value));
        }

        @Override
        public void writeFloat(String key, float value) {
            this.data.writeName(key);
            this.data.writeInt32(Float.floatToIntBits(value));
        }

        @Override
        public void writeDouble(String key, double value) {
            this.data.writeName(key);
            this.data.writeDouble(value);
        }

        @Override
        public void writeUUID(String key, UUID value) {
            this.data.writeName(key);
            this.codecRegistry.get(UUID.class).encode(this.data, value, this.encoderContext);
        }

        @Override
        public <K, V> void writeMap(String key, Map<K, V> value, Function<K, String> keyMapper, BiConsumer<String, V> valueWriter) {
            this.data.writeName(key);
            this.data.writeStartDocument();
            for (Map.Entry<K, V> entry : value.entrySet()) {
                valueWriter.accept(keyMapper.apply(entry.getKey()), entry.getValue());
            }
            this.data.writeEndDocument();
        }

        @Override
        public <K, V> void writeMultiMap(String key, Multimap<K, V> value, Function<K, String> keyMapper, BiConsumer<String, Collection<V>> valueWriter) {
            this.data.writeName(key);
            this.data.writeStartDocument();
            for (Map.Entry<K, Collection<V>> entry : value.asMap().entrySet()) {
                valueWriter.accept(keyMapper.apply(entry.getKey()), entry.getValue());
            }
            this.data.writeEndDocument();
        }

        @Override
        public <V> void writeList(String key, Collection<V> value, Function<V, String> valueMapper) {
            this.data.writeName(key);
            Collection<String> values = new ArrayList<>();
            for (V v : value) {
                values.add(valueMapper.apply(v));
            }
            this.codecRegistry.get(Collection.class).encode(this.data, values, this.encoderContext);
        }

        @Override
        public void writePersistentStat(String key, PersistentStat<? extends Number> value) throws Exception {
            this.data.writeName(key);
            this.data.writeStartDocument();
            value.save(this);
            this.data.writeEndDocument();
        }

        @Override
        public void writeDuration(String key, Duration value) {
            this.data.writeName(key);
            this.data.writeInt64(value.toMicros());
        }

        @Override
        public void writeInstant(String key, Instant value) {
            this.data.writeName(key);
            this.codecRegistry.get(Instant.class).encode(this.data, value, this.encoderContext);
        }

        @Override
        public <E extends Enum<E>> void writeEnum(String key, E value) {
            this.writeString(key, value.name());
        }

    }

    public static class MongoDataReader extends DataReader<BsonReader> {

        private final CodecRegistry codecRegistry;
        private final DecoderContext decoderContext;

        public MongoDataReader(BsonReader data, CodecRegistry registries, DecoderContext decoderContext) {
            super(data);
            this.codecRegistry = registries;
            this.decoderContext = decoderContext;
        }

        @Override
        public char readChar(String key) {
            return Character.forDigit(this.handle.readInt32(key), 10);
        }

        @Override
        public String readString(String key) {
            return this.handle.readString(key);
        }

        @Override
        public boolean readBoolean(String key) {
            return this.handle.readBoolean(key);
        }

        @Override
        public byte readByte(String key) {
            return (byte) this.handle.readInt32(key);
        }

        @Override
        public int readInt(String key) {
            return this.handle.readInt32(key);
        }

        @Override
        public long readLong(String key) {
            return this.handle.readInt64(key);
        }

        @Override
        public short readShort(String key) {
            return (short) this.handle.readInt32(key);
        }

        @Override
        public float readFloat(String key) {
            return Float.intBitsToFloat(this.handle.readInt32(key));
        }

        @Override
        public double readDouble(String key) {
            return this.handle.readDouble(key);
        }

        @Override
        public UUID readUUID(String key) {
            this.handle.readName(key);
            return this.codecRegistry.get(UUID.class).decode(this.handle, this.decoderContext);
        }

        @Override
        public <K, V> Map<K, V> readMap(String key, Function<String, K> keyMapper, Function<String, V> reader) {
            Map<K, V> map = new HashMap<>();
            this.handle.readName(key);
            this.handle.readStartDocument();
            while (this.handle.readBsonType() != BsonType.END_OF_DOCUMENT) {
                String name = this.handle.readName();
                map.put(keyMapper.apply(name), reader.apply(name));
            }
            this.handle.readEndDocument();
            return map;
        }

        @Override
        public <K, V> Multimap<K, V> readMultiMap(String key, Function<String, K> keyMapper, Function<String, Collection<V>> reader) throws Exception {
            Multimap<K, V> map = ArrayListMultimap.create();
            this.handle.readName(key);
            this.handle.readStartDocument();
            while (this.handle.readBsonType() != BsonType.END_OF_DOCUMENT) {
                String name = this.handle.readName();
                map.putAll(keyMapper.apply(name), reader.apply(name));
            }
            this.handle.readEndDocument();
            return map;
        }

        @Override
        public <V> Collection<V> readList(String key, Function<String, V> valueMapper) {
            this.handle.readName(key);
            Collection<V> list = new ArrayList<>();

            Collection decode = this.codecRegistry.get(Collection.class).decode(this.handle, this.decoderContext);
            for (Object o : decode) {
                list.add(valueMapper.apply((String) o));
            }
            return list;
        }

        @Override
        public <N extends Number> PersistentStat<N> readPersistentStat(String key, Class<N> type) throws Exception {
            this.handle.readName(key);
            PersistentStat<N> stat = new PersistentStat<>(key);
            stat.load(this);
            return stat;
        }

        @Override
        public Duration readDuration(String key) {
            return Duration.ofMicroseconds(this.readLong(key));
        }

        @Override
        public Instant readInstant(String key) {
            return this.codecRegistry.get(Instant.class).decode(this.handle, this.decoderContext);
        }

        @Override
        public <E extends Enum<E>> E readEnum(String key, Class<E> type) {
            return EnumUtil.matchEnum(this.handle.readString(key), type.getEnumConstants());
        }

    }

}
