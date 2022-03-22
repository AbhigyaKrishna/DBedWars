package org.zibble.dbedwars.database.data;

import com.google.gson.JsonElement;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.api.util.Keyed;
import org.zibble.dbedwars.api.util.json.JSONBuilder;
import org.zibble.dbedwars.api.util.json.Json;

import java.util.function.Function;

public class PersistentStat<T extends Number> implements Keyed<String> {

    public static <T extends Number> PersistentStat<T> from(String key, Json json, Function<JsonElement, T> mapper) {
        PersistentStat<T> stat = new PersistentStat<>(key);
        stat.total = mapper.apply(json.get("total"));
        stat.monthly = mapper.apply(json.get("monthly"));
        stat.weekly = mapper.apply(json.get("weekly"));
        stat.daily = mapper.apply(json.get("daily"));
        return stat;
    }

    private final Key<String> key;

    private T total;
    private T monthly;
    private T weekly;
    private T daily;

    public PersistentStat(String key) {
        this.key = Key.of(key);
    }

    @Override
    public Key<String> getKey() {
        return key;
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

}
