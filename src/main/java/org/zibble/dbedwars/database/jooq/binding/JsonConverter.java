package org.zibble.dbedwars.database.jooq.binding;

import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.JSON;
import org.zibble.dbedwars.api.util.json.Json;

public class JsonConverter implements Converter<JSON, Json> {

    public static final JsonConverter CONVERTER = new JsonConverter();

    @Override
    public Json from(JSON databaseObject) {
        return databaseObject == null ? null : Json.loadFromString(databaseObject.data());
    }

    @Override
    public JSON to(Json userObject) {
        return userObject == null ? null : JSON.json(userObject.toString());
    }

    @Override
    public @NotNull Class<JSON> fromType() {
        return JSON.class;
    }

    @Override
    public @NotNull Class<Json> toType() {
        return Json.class;
    }

}
