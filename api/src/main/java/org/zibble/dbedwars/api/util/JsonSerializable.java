package org.zibble.dbedwars.api.util;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.util.json.Json;

public interface JsonSerializable {

    Json toJson();

    void fromJson(@NotNull Json json);

    default String toJsonString(){
        return toJson().toString();
    }
}
