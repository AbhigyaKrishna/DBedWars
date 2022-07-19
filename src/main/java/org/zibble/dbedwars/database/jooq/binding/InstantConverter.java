package org.zibble.dbedwars.database.jooq.binding;

import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;

import java.sql.Timestamp;
import java.time.Instant;

public class InstantConverter implements Converter<Timestamp, Instant> {

    @Override
    public Instant from(Timestamp databaseObject) {
        return databaseObject.toInstant();
    }

    @Override
    public Timestamp to(Instant userObject) {
        return Timestamp.from(userObject);
    }

    @Override
    public @NotNull Class<Timestamp> fromType() {
        return Timestamp.class;
    }

    @Override
    public @NotNull Class<Instant> toType() {
        return Instant.class;
    }

}
