package org.zibble.dbedwars.cache;

import com.google.gson.JsonElement;
import com.google.gson.stream.MalformedJsonException;
import org.zibble.dbedwars.api.util.json.Json;
import org.zibble.dbedwars.configuration.PluginFiles;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private static final Map<String, String> HEAD_CACHE = new HashMap<>();

    public static String getHead(String entityType) {
        return HEAD_CACHE.get(entityType);
    }

    public static void load() throws MalformedJsonException {
        Json json = Json.load(PluginFiles.CACHE.HEADS);
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            HEAD_CACHE.put(entry.getKey(), entry.getValue().getAsString());
        }
    }

}
