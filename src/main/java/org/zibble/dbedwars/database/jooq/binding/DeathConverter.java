package org.zibble.dbedwars.database.jooq.binding;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.JSON;
import org.zibble.dbedwars.api.game.DeathCause;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.api.util.json.JSONBuilder;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.database.data.ArenaHistory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathConverter implements Converter<JSON, Map<UUID, ArenaHistory.DeathData>> {

    @Override
    public Map<UUID, ArenaHistory.DeathData> from(JSON databaseObject) {
        Json json = Json.loadFromString(databaseObject.data());
        Map<UUID, ArenaHistory.DeathData> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            UUID uuid = UUID.fromString(entry.getKey());
            ArenaHistory.DeathData deathData = new ArenaHistory.DeathData();
            JsonObject sub = entry.getValue().getAsJsonObject();
            deathData.setKiller(UUID.fromString(sub.get("killer").getAsString()));
            deathData.setCause(EnumUtil.matchEnum(sub.get("cause").getAsString(), DeathCause.VALUES));
            map.put(uuid, deathData);
        }
        return map;
    }

    @Override
    public JSON to(Map<UUID, ArenaHistory.DeathData> userObject) {
        JSONBuilder builder = new JSONBuilder();
        for (Map.Entry<UUID, ArenaHistory.DeathData> entry : userObject.entrySet()) {
            builder.add(entry.getKey().toString(), new JSONBuilder()
                    .add("killer", entry.getValue().getKiller().toString())
                    .add("cause", entry.getValue().getCause().name())
                    .toJson());
        }
        return JSON.valueOf(builder.toString());
    }

    @Override
    public @NotNull Class<JSON> fromType() {
        return JSON.class;
    }

    @Override
    public @NotNull Class<Map<UUID, ArenaHistory.DeathData>> toType() {
        return (Class<Map<UUID, ArenaHistory.DeathData>>) (Class<?>) Map.class;
    }

}
