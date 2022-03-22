package org.zibble.dbedwars.database.jooq.binding;

import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.JSON;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.database.data.QuickBuyData;

public class QuickBuyConverter implements Converter<JSON, QuickBuyData> {

    @Override
    public QuickBuyData from(JSON databaseObject) {
        Json json = Json.loadFromString(databaseObject.data());
        return QuickBuyData.from(json);
    }

    @Override
    public JSON to(QuickBuyData userObject) {
        return JSON.valueOf(userObject.toJson().toString());
    }

    @Override
    public @NotNull Class<JSON> fromType() {
        return JSON.class;
    }

    @Override
    public @NotNull Class<QuickBuyData> toType() {
        return QuickBuyData.class;
    }

}
