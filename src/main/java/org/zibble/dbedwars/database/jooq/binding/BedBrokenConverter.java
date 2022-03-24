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

import java.util.*;

public class BedBrokenConverter implements Converter<JSON, Multimap<UUID, Color>> {

    @Override
    public Multimap<UUID, Color> from(JSON databaseObject) {
        Json json = Json.loadFromString(databaseObject.data());
        Multimap<UUID, Color> map = ArrayListMultimap.create();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            List<Color> colors = new ArrayList<>();
            for (JsonElement element : entry.getValue().getAsJsonArray()) {
                colors.add(EnumUtil.matchEnum(element.getAsString(), Color.VALUES));
            }
            map.putAll(UUID.fromString(entry.getKey()), colors);
        }
        return map;
    }

    @Override
    public JSON to(Multimap<UUID, Color> userObject) {
        JSONBuilder builder = new JSONBuilder();
        for (Map.Entry<UUID, Collection<Color>> entry : userObject.asMap().entrySet()) {
            ArrayList<Color> c = new ArrayList<>(entry.getValue());
            String[] colors = new String[entry.getValue().size()];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = c.get(i).name();
            }
            builder.add(entry.getKey().toString(), colors);
        }
        return JSON.valueOf(builder.toString());
    }

    @Override
    public @NotNull Class<JSON> fromType() {
        return JSON.class;
    }

    @Override
    public @NotNull Class<Multimap<UUID, Color>> toType() {
        return (Class<Multimap<UUID, Color>>) (Class<?>) Multimap.class;
    }

}
