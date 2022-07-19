package org.zibble.dbedwars.database.data;

import com.google.gson.JsonElement;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.zibble.dbedwars.api.objects.points.Count;
import org.zibble.dbedwars.api.util.DataType;
import org.zibble.dbedwars.api.util.json.JSONBuilder;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.Keyed;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

import java.util.function.Supplier;

public class PersistentStat<T extends Number> implements DataCache, Keyed {

    public static final Key TOTAL = Key.of("total");
    public static final Key MONTHLY = Key.of("monthly");
    public static final Key WEEKLY = Key.of("weekly");
    public static final Key DAILY = Key.of("daily");

    @BsonIgnore
    private final transient Key key;
    private Count<T> total;
    private Count<T> monthly;
    private Count<T> weekly;
    private Count<T> daily;
    public PersistentStat(Key key, Supplier<Count<T>> supplier) {
        this.key = key;
        this.total = supplier.get();
        this.monthly = supplier.get();
        this.weekly = supplier.get();
        this.daily = supplier.get();
    }

    public static <T extends Number> PersistentStat<T> from(Key key, Json json, java.util.function.Function<JsonElement, Count<T>> mapper) {
        PersistentStat<T> stat = new PersistentStat<>(key, () -> null);
        stat.total = mapper.apply(json.get(TOTAL.get()));
        stat.monthly = mapper.apply(json.get(MONTHLY.get()));
        stat.weekly = mapper.apply(json.get(WEEKLY.get()));
        stat.daily = mapper.apply(json.get(DAILY.get()));
        return stat;
    }

    @Override
    public Key getKey() {
        return this.key;
    }

    public Count<T> getTotal() {
        return total;
    }

    public void setTotal(Count<T> total) {
        this.total = total;
    }

    public Count<T> getMonthly() {
        return monthly;
    }

    public void setMonthly(Count<T> monthly) {
        this.monthly = monthly;
    }

    public Count<T> getWeekly() {
        return weekly;
    }

    public void setWeekly(Count<T> weekly) {
        this.weekly = weekly;
    }

    public Count<T> getDaily() {
        return daily;
    }

    public void setDaily(Count<T> daily) {
        this.daily = daily;
    }

    public Json toJson() {
        return new JSONBuilder()
                .add(TOTAL.get(), this.total.get())
                .add(MONTHLY.get(), this.monthly.get())
                .add(WEEKLY.get(), this.weekly.get())
                .add(DAILY.get(), this.daily.get())
                .toJson();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void load(DataReader<?> reader) throws Exception {
        DataType type = DataType.fromClass(this.total.getClass());
        this.total = reader.readCount(TOTAL, type);
        this.monthly = reader.readCount(MONTHLY, type);
        this.weekly = reader.readCount(WEEKLY, type);
        this.daily = reader.readCount(DAILY, type);
    }

    @Override
    public void save(DataWriter<?> writer) throws Exception {
        writer.writeCount(TOTAL, this.total);
        writer.writeCount(MONTHLY, this.monthly);
        writer.writeCount(WEEKLY, this.weekly);
        writer.writeCount(DAILY, this.daily);
    }

    @Override
    public PersistentStat<T> copy() {
        PersistentStat<T> stat = new PersistentStat<>(this.key, () -> null);
        stat.total = this.total.clone();
        stat.monthly = this.monthly.clone();
        stat.weekly = this.weekly.clone();
        stat.daily = this.daily.clone();
        return stat;
    }

    @Override
    public NamedProperties toProperties() {
        return NamedProperties.builder()
                .add(TOTAL, this.total)
                .add(MONTHLY, this.monthly)
                .add(WEEKLY, this.weekly)
                .add(DAILY, this.daily)
                .build();
    }

    interface BiConsumer<T, U> {

        void accept(T t, U u) throws Exception;

    }

    interface Function<T, U> {

        U apply(T t) throws Exception;

    }

}
