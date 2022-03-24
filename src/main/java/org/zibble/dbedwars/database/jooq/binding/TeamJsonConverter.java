package org.zibble.dbedwars.database.jooq.binding;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.JSON;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.api.util.json.JSONBuilder;
import org.zibble.dbedwars.api.util.json.Json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class TeamJsonConverter implements Converter<JSON, Multimap<Color, UUID>> {

    @Override
    public Multimap<Color, UUID> from(JSON databaseObject) {
        Json json = Json.loadFromString(databaseObject.data());
        Multimap<Color, UUID> map = ArrayListMultimap.create();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            ArrayList<UUID> list = new ArrayList<>();
            for (JsonElement element : entry.getValue().getAsJsonArray()) {
                list.add(UUID.fromString(element.getAsString()));
            }
            map.putAll(EnumUtil.matchEnum(entry.getKey(), Color.VALUES), list);
        }
        return map;
    }

    @Override
    public JSON to(Multimap<Color, UUID> userObject) {
        JSONBuilder builder = new JSONBuilder();
        for (Map.Entry<Color, Collection<UUID>> entry : userObject.asMap().entrySet()) {
            ArrayList<UUID> temp = new ArrayList<>(entry.getValue());
            String[] uuids = new String[entry.getValue().size()];
            for (int i = 0; i < uuids.length; i++) {
                uuids[i] = temp.get(i).toString();
            }
            builder.addNewList(entry.getKey().name(), uuids);
        }
        return JSON.valueOf(builder.toString());
    }

    @Override
    public @NotNull Class<JSON> fromType() {
        return JSON.class;
    }

    @Override
    public @NotNull Class<Multimap<Color, UUID>> toType() {
        return (Class<Multimap<Color, UUID>>) (Class<?>) Multimap.class;
    }

}
