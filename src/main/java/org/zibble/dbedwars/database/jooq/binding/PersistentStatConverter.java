package org.zibble.dbedwars.database.jooq.binding;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.JSON;
import org.zibble.dbedwars.api.objects.points.Count;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.database.data.PersistentStat;

import java.util.function.Function;

public class PersistentStatConverter<T extends Number> implements Converter<JSON, PersistentStat<T>> {

    private final Key key;
    private final Function<JsonElement, Count<T>> mapper;

    public PersistentStatConverter(Key key, Function<JsonElement, Count<T>> mapper) {
        this.key = key;
        this.mapper = mapper;
    }

    @Override
    public PersistentStat<T> from(JSON databaseObject) {
        return databaseObject == null ? null : PersistentStat.from(this.key, Json.loadFromString(databaseObject.data()), this.mapper);
    }

    @Override
    public JSON to(PersistentStat<T> userObject) {
        return userObject == null ? null : JSON.valueOf(userObject.toJson().toString());
    }

    @Override
    public @NotNull Class<JSON> fromType() {
        return JSON.class;
    }

    @Override
    public @NotNull Class<PersistentStat<T>> toType() {
        return (Class<PersistentStat<T>>) (Class<?>) PersistentStat.class;
    }

}
