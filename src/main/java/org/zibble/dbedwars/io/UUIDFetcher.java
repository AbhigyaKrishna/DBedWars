package org.zibble.dbedwars.io;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UUIDFetcher {

    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";
    private final Cache<String, UUID> uuidCache = CacheBuilder.newBuilder().expireAfterAccess(120, TimeUnit.MINUTES).build();
    private final Cache<UUID, String> nameCache = CacheBuilder.newBuilder().expireAfterAccess(120, TimeUnit.MINUTES).build();

    private static final UUIDFetcher INSTANCE = new UUIDFetcher();

    private UUIDFetcher() {}

    public UUID getUUID(String name) {
        return getUUID(name, false);
    }

    public UUID getUUID(String name, boolean forceNew) {
        return getUUID(name, System.currentTimeMillis(), forceNew);
    }

    public UUID getUUID(String name, long timestamp, boolean forceNew) {
        name = name.toLowerCase();

        UUID uuid = uuidCache.getIfPresent(name);

        if (uuid != null && !forceNew) return uuid;

        try {
            // Open connection
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(UUID_URL, name, timestamp / 1000)).openConnection();
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Parse response
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

                    // Parse response
                    JsonObject data = new JsonParser().parse(bufferedReader).getAsJsonObject();
                    uuid = UUIDTypeAdaptor.fromString(data.get("id").getAsString());

                    // Cache data
                    uuidCache.put(name, uuid);
                    nameCache.put(uuid, data.get("name").getAsString());
                }
            }

            connection.disconnect();
        } catch (IOException ignored) {
        }

        return uuid;
    }

    public String getName(UUID uuid, boolean forceNew) {
        String name = nameCache.getIfPresent(uuid);

        if (name != null && !forceNew) return name;

        try {
            // Open connection
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(NAME_URL, UUIDTypeAdaptor.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);

            // Parse response
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

                    JsonArray data = new JsonParser().parse(bufferedReader).getAsJsonArray();
                    JsonObject ctx = data.get(data.size() - 1).getAsJsonObject();

                    name = ctx.get("name").getAsString();

                    // Cache data
                    uuidCache.put(name, uuid);
                    nameCache.put(uuid, name);
                }
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return name;
    }

    public static UUIDFetcher getInstance() {
        return INSTANCE;
    }
}
