package org.zibble.dbedwars.database.jooq.binding;

import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.types.YearToSecond;
import org.zibble.dbedwars.api.objects.serializable.Duration;

public class DurationConverter implements Converter<YearToSecond, Duration> {

    @Override
    public Duration from(YearToSecond databaseObject) {
        return Duration.fromJava(databaseObject.toDuration());
    }

    @Override
    public YearToSecond to(Duration userObject) {
        return YearToSecond.valueOf(userObject.toMillis());
    }

    @Override
    public @NotNull Class<YearToSecond> fromType() {
        return YearToSecond.class;
    }

    @Override
    public @NotNull Class<Duration> toType() {
        return Duration.class;
    }

}
