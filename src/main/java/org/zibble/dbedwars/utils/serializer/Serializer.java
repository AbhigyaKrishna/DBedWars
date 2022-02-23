package org.zibble.dbedwars.utils.serializer;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.utils.json.Json;

public interface Serializer<T> {

    String serialize(@NotNull final T object);

    T deserialize(@NotNull Json json);

    Class<T> clazz();
}
