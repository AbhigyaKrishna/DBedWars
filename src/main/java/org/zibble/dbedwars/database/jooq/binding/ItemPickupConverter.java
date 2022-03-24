package org.zibble.dbedwars.database.jooq.binding;

import com.cryptomorin.xseries.XMaterial;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.JSON;
import org.zibble.dbedwars.api.util.json.JSONBuilder;
import org.zibble.dbedwars.api.util.json.Json;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemPickupConverter implements Converter<JSON, Map<UUID, Map<XMaterial, Integer>>> {

    @Override
    public Map<UUID, Map<XMaterial, Integer>> from(JSON databaseObject) {
        Json json = Json.loadFromString(databaseObject.data());
        Map<UUID, Map<XMaterial, Integer>> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            UUID uuid = UUID.fromString(entry.getKey());
            Map<XMaterial, Integer> itemMap = new HashMap<>();
            for (Map.Entry<String, JsonElement> itemEntry : entry.getValue().getAsJsonObject().entrySet()) {
                XMaterial material = XMaterial.matchXMaterial(itemEntry.getKey()).get();
                itemMap.put(material, itemEntry.getValue().getAsInt());
            }
            map.put(uuid, itemMap);
        }
        return map;
    }

    @Override
    public JSON to(Map<UUID, Map<XMaterial, Integer>> userObject) {
        JSONBuilder builder = new JSONBuilder();
        for (Map.Entry<UUID, Map<XMaterial, Integer>> entry : userObject.entrySet()) {
            Json sub = Json.getNew();
            for (Map.Entry<XMaterial, Integer> itemEntry : entry.getValue().entrySet()) {
                sub.addProperty(itemEntry.getKey().name(), itemEntry.getValue());
            }
            builder.add(entry.getKey().toString(), sub);
        }
        return JSON.valueOf(builder.toString());
    }

    @Override
    public @NotNull Class<JSON> fromType() {
        return JSON.class;
    }

    @Override
    public @NotNull Class<Map<UUID, Map<XMaterial, Integer>>> toType() {
        return (Class<Map<UUID, Map<XMaterial, Integer>>>) (Class<?>) Map.class;
    }

}
