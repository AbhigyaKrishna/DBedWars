package org.zibble.dbedwars.utils.serializer;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.utils.json.Json;

public interface Serializer<T> {

    Json serialize(@NotNull final T object);

    T deserialize(@NotNull Json json);

    default String toString(@NotNull final  T object){
        return serialize(object).toString();
    }

    Class<T> clazz();
}
