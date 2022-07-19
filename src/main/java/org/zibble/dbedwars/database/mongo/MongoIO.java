package org.zibble.dbedwars.database.mongo;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.zibble.dbedwars.api.objects.points.Count;
import org.zibble.dbedwars.api.objects.points.DoubleCount;
import org.zibble.dbedwars.api.objects.points.IntegerCount;
import org.zibble.dbedwars.api.objects.points.LongCount;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.util.DataType;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.api.util.key.Key;
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
        public void writeChar(Key key, char value) {
            this.data.writeName(key.get());
            this.data.writeInt32(Character.getNumericValue(value));
        }

        @Override
        public void writeString(Key key, String value) {
            this.data.writeName(key.get());
            this.data.writeString(value);
        }

        @Override
        public void writeBoolean(Key key, boolean value) {
            this.data.writeName(key.get());
            this.data.writeBoolean(value);
        }

        @Override
        public void writeByte(Key key, byte value) {
            this.data.writeName(key.get());
            this.data.writeInt32(Byte.toUnsignedInt(value));
        }

        @Override
        public void writeInt(Key key, int value) {
            this.data.writeName(key.get());
            this.data.writeInt32(value);
        }

        @Override
        public void writeLong(Key key, long value) {
            this.data.writeName(key.get());
            this.data.writeInt64(value);
        }

        @Override
        public void writeShort(Key key, short value) {
            this.data.writeName(key.get());
            this.data.writeInt32(Short.toUnsignedInt(value));
        }

        @Override
        public void writeFloat(Key key, float value) {
            this.data.writeName(key.get());
            this.data.writeInt32(Float.floatToIntBits(value));
        }

        @Override
        public void writeDouble(Key key, double value) {
            this.data.writeName(key.get());
            this.data.writeDouble(value);
        }

        @Override
        public <R extends Number> void writeCount(Key key, Count<R> value) throws Exception {
            switch (value.getType()) {
                case BYTE:
                    this.writeByte(key, value.byteValue());
                    break;
                case SHORT:
                    this.writeShort(key, value.shortValue());
                    break;
                case INTEGER:
                    this.writeInt(key, value.intValue());
                    break;
                case LONG:
                    this.writeLong(key, value.longValue());
                    break;
                case FLOAT:
                    this.writeFloat(key, value.floatValue());
                    break;
                case DOUBLE:
                    this.writeDouble(key, value.doubleValue());
                    break;
                default:
                    throw new Exception("Unsupported count type: " + value.getType());
            }
        }

        @Override
        public void writeUUID(Key key, UUID value) {
            this.data.writeName(key.get());
            this.codecRegistry.get(UUID.class).encode(this.data, value, this.encoderContext);
        }

        @Override
        public <K, V> void writeMap(Key key, Map<K, V> value, Function<K, String> keyMapper, BiConsumer<String, V> valueWriter) {
            this.data.writeName(key.get());
            this.data.writeStartDocument();
            for (Map.Entry<K, V> entry : value.entrySet()) {
                valueWriter.accept(keyMapper.apply(entry.getKey()), entry.getValue());
            }
            this.data.writeEndDocument();
        }

        @Override
        public <K, V> void writeMultiMap(Key key, Multimap<K, V> value, Function<K, String> keyMapper, BiConsumer<String, Collection<V>> valueWriter) {
            this.data.writeName(key.get());
            this.data.writeStartDocument();
            for (Map.Entry<K, Collection<V>> entry : value.asMap().entrySet()) {
                valueWriter.accept(keyMapper.apply(entry.getKey()), entry.getValue());
            }
            this.data.writeEndDocument();
        }

        @Override
        public <V> void writeList(Key key, Collection<V> value, Function<V, String> valueMapper) {
            this.data.writeName(key.get());
            Collection<String> values = new ArrayList<>();
            for (V v : value) {
                values.add(valueMapper.apply(v));
            }
            this.codecRegistry.get(Collection.class).encode(this.data, values, this.encoderContext);
        }

        @Override
        public void writePersistentStat(Key key, PersistentStat<? extends Number> value) throws Exception {
            this.data.writeName(key.get());
            this.data.writeStartDocument();
            value.save(this);
            this.data.writeEndDocument();
        }

        @Override
        public void writeDuration(Key key, Duration value) {
            this.data.writeName(key.get());
            this.data.writeInt64(value.toMicros());
        }

        @Override
        public void writeInstant(Key key, Instant value) {
            this.data.writeName(key.get());
            this.codecRegistry.get(Instant.class).encode(this.data, value, this.encoderContext);
        }

        @Override
        public <E extends Enum<E>> void writeEnum(Key key, E value) {
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
        public char readChar(Key key) {
            return Character.forDigit(this.handle.readInt32(key.get()), 10);
        }

        @Override
        public String readString(Key key) {
            return this.handle.readString(key.get());
        }

        @Override
        public boolean readBoolean(Key key) {
            return this.handle.readBoolean(key.get());
        }

        @Override
        public byte readByte(Key key) {
            return (byte) this.handle.readInt32(key.get());
        }

        @Override
        public int readInt(Key key) {
            return this.handle.readInt32(key.get());
        }

        @Override
        public long readLong(Key key) {
            return this.handle.readInt64(key.get());
        }

        @Override
        public short readShort(Key key) {
            return (short) this.handle.readInt32(key.get());
        }

        @Override
        public float readFloat(Key key) {
            return Float.intBitsToFloat(this.handle.readInt32(key.get()));
        }

        @Override
        public double readDouble(Key key) {
            return this.handle.readDouble(key.get());
        }

        @Override
        public <R extends Number> Count<R> readCount(Key key, DataType type) throws Exception {
            switch (type) {
                case INTEGER:
                case BYTE:
                case SHORT:
                    return (Count<R>) new IntegerCount(this.readInt(key));
                case LONG:
                    return (Count<R>) new LongCount(this.readLong(key));
                case FLOAT:
                case DOUBLE:
                    return (Count<R>) new DoubleCount(this.readDouble(key));
            }
            throw new IllegalArgumentException("Unsupported data type: " + type);
        }

        @Override
        public UUID readUUID(Key key) {
            this.handle.readName(key.get());
            return this.codecRegistry.get(UUID.class).decode(this.handle, this.decoderContext);
        }

        @Override
        public <K, V> Map<K, V> readMap(Key key, Function<String, K> keyMapper, Function<String, V> reader) {
            Map<K, V> map = new HashMap<>();
            this.handle.readName(key.get());
            this.handle.readStartDocument();
            while (this.handle.readBsonType() != BsonType.END_OF_DOCUMENT) {
                String name = this.handle.readName();
                map.put(keyMapper.apply(name), reader.apply(name));
            }
            this.handle.readEndDocument();
            return map;
        }

        @Override
        public <K, V> Multimap<K, V> readMultiMap(Key key, Function<String, K> keyMapper, Function<String, Collection<V>> reader) throws Exception {
            Multimap<K, V> map = ArrayListMultimap.create();
            this.handle.readName(key.get());
            this.handle.readStartDocument();
            while (this.handle.readBsonType() != BsonType.END_OF_DOCUMENT) {
                String name = this.handle.readName();
                map.putAll(keyMapper.apply(name), reader.apply(name));
            }
            this.handle.readEndDocument();
            return map;
        }

        @Override
        public <V> Collection<V> readList(Key key, Function<String, V> valueMapper) {
            this.handle.readName(key.get());
            Collection<V> list = new ArrayList<>();

            Collection decode = this.codecRegistry.get(Collection.class).decode(this.handle, this.decoderContext);
            for (Object o : decode) {
                list.add(valueMapper.apply((String) o));
            }
            return list;
        }

        @Override
        public <N extends Number> PersistentStat<N> readPersistentStat(Key key, Class<N> type) throws Exception {
            this.handle.readName(key.get());
            PersistentStat<N> stat = new PersistentStat<>(key, () -> null);
            stat.load(this);
            return stat;
        }

        @Override
        public Duration readDuration(Key key) {
            return Duration.ofMicroseconds(this.readLong(key));
        }

        @Override
        public Instant readInstant(Key key) {
            return this.codecRegistry.get(Instant.class).decode(this.handle, this.decoderContext);
        }

        @Override
        public <E extends Enum<E>> E readEnum(Key key, Class<E> type) {
            return EnumUtil.matchEnum(this.handle.readString(key.get()), type.getEnumConstants());
        }

    }

}
