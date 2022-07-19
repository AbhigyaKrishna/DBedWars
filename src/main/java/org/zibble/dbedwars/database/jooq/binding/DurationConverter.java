package org.zibble.dbedwars.database.jooq.binding;

import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.zibble.dbedwars.api.objects.serializable.Duration;

public class DurationConverter implements Converter<UInteger, Duration> {

    @Override
    public Duration from(UInteger databaseObject) {
        return Duration.ofMilliseconds(databaseObject.longValue());
    }

    @Override
    public UInteger to(Duration userObject) {
        return UInteger.valueOf(userObject.toMillis());
    }

    @Override
    public @NotNull Class<UInteger> fromType() {
        return UInteger.class;
    }

    @Override
    public @NotNull Class<Duration> toType() {
        return Duration.class;
    }

}
