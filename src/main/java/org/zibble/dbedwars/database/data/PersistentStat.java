package org.zibble.dbedwars.database.data;

import com.google.gson.JsonElement;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.zibble.dbedwars.api.util.DataType;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.Keyed;
import org.zibble.dbedwars.api.util.json.JSONBuilder;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

public class PersistentStat<T extends Number> implements DataCache, Keyed<String> {

    public static <T extends Number> PersistentStat<T> from(String key, Json json, java.util.function.Function<JsonElement, T> mapper) {
        PersistentStat<T> stat = new PersistentStat<>(key);
        stat.total = mapper.apply(json.get("total"));
        stat.monthly = mapper.apply(json.get("monthly"));
        stat.weekly = mapper.apply(json.get("weekly"));
        stat.daily = mapper.apply(json.get("daily"));
        return stat;
    }

    @BsonIgnore
    private final String key;

    private T total;
    private T monthly;
    private T weekly;
    private T daily;

    public PersistentStat(String key) {
        this.key = key;
    }

    @Override
    public Key<String> getKey() {
        return Key.of(this.key);
    }

    public T getTotal() {
        return total;
    }

    public void setTotal(T total) {
        this.total = total;
    }

    public T getMonthly() {
        return monthly;
    }

    public void setMonthly(T monthly) {
        this.monthly = monthly;
    }

    public T getWeekly() {
        return weekly;
    }

    public void setWeekly(T weekly) {
        this.weekly = weekly;
    }

    public T getDaily() {
        return daily;
    }

    public void setDaily(T daily) {
        this.daily = daily;
    }

    public Json toJson() {
        return new JSONBuilder()
                .add("total", this.total)
                .add("monthly", this.monthly)
                .add("weekly", this.weekly)
                .add("daily", this.daily)
                .toJson();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void load(DataReader<?> reader) throws Exception {
        DataType type = DataType.fromClass(this.total.getClass());
        Function<String, T> readFunction;
        switch (type) {
            case INTEGER:
                readFunction = (s) -> (T) (Integer) reader.readInt(s);
                break;
            case LONG:
                readFunction = (s) -> (T) (Long) reader.readLong(s);
                break;
            case FLOAT:
                readFunction = (s) -> (T) (Float) reader.readFloat(s);
                break;
            case DOUBLE:
            default:
                readFunction = (s) -> (T) (Double) reader.readDouble(s);
        }
        this.total = readFunction.apply("total");
        this.monthly = readFunction.apply("monthly");
        this.weekly = readFunction.apply("weekly");
        this.daily = readFunction.apply("daily");
    }

    @Override
    public void save(DataWriter<?> writer) throws Exception {
        DataType type = DataType.fromClass(this.total.getClass());
        BiConsumer<T, String> writeFunction;
        switch (type) {
            case INTEGER:
                writeFunction = (o, s) -> writer.writeInt(s, (Integer) o);
                break;
            case LONG:
                writeFunction = (o, s) -> writer.writeLong(s, (Long) o);
                break;
            case FLOAT:
                writeFunction = (o, s) -> writer.writeFloat(s, (Float) o);
                break;
            case DOUBLE:
            default:
                writeFunction = (o, s) -> writer.writeDouble(s, (Double) o);
        }
        writeFunction.accept(this.total, "total");
        writeFunction.accept(this.monthly, "monthly");
        writeFunction.accept(this.weekly, "weekly");
        writeFunction.accept(this.daily, "daily");
    }

    @Override
    public PersistentStat<T> copy() {
        PersistentStat<T> stat = new PersistentStat<>(this.key);
        stat.total = this.total;
        stat.monthly = this.monthly;
        stat.weekly = this.weekly;
        stat.daily = this.daily;
        return stat;
    }

    @Override
    public NamedProperties toProperties() {
        return NamedProperties.builder()
                .add("total", this.total)
                .add("monthly", this.monthly)
                .add("weekly", this.weekly)
                .add("daily", this.daily)
                .build();
    }

    interface BiConsumer<T, U> {

        void accept(T t, U u) throws Exception;

    }

    interface Function<T, U> {

        U apply(T t) throws Exception;

    }

}
