package org.zibble.dbedwars.io;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.*;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.zibble.dbedwars.api.objects.profile.PlayerGameProfile;
import org.zibble.dbedwars.api.objects.profile.Property;
import org.zibble.dbedwars.api.objects.profile.Skin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GameProfileFetcher {

    private static final String SERVICE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    private static final String JSON_SKIN = "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}";
    private static final String JSON_CAPE = "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"},\"CAPE\":{\"url\":\"%s\"}}}";
    private static final GameProfileFetcher INSTANCE = new GameProfileFetcher();
    private final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(UUID.class, new UUIDTypeAdaptor())
            .registerTypeAdapter(PlayerGameProfile.class, new GameProfileSerializer())
            .create();
    private final Cache<UUID, PlayerGameProfile> cache = CacheBuilder.newBuilder().expireAfterWrite(120, TimeUnit.MINUTES).build();

    public static GameProfileFetcher getInstance() {
        return INSTANCE;
    }

    public PlayerGameProfile fetch(UUID uuid) {
        return fetch(uuid, false);
    }

    public PlayerGameProfile fetch(UUID uuid, boolean forceNew) {
        // Check for cached profile
        PlayerGameProfile profile = cache.getIfPresent(uuid);
        if (!forceNew && profile != null)
            return profile;
        else {
            try {
                // Open http connection
                HttpURLConnection connection = (HttpURLConnection) new URL(String.format(SERVICE_URL, UUIDTypeAdaptor.fromUUID(uuid))).openConnection();
                connection.setReadTimeout(5000);

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // Parse response
                    String json = "", line;
                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        while ((line = bufferedReader.readLine()) != null) json += line;

                        profile = gson.fromJson(json, PlayerGameProfile.class);
                        // Cache profile
                        cache.put(uuid, profile);
                    }

                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return profile;
        }
    }

    public PlayerGameProfile getProfile(UUID uuid, String name, String skin) {
        return getProfile(uuid, name, skin, null);
    }

    public PlayerGameProfile getProfile(UUID uuid, String name, String skinUrl, String capeUrl) {
        // Create profile from properties
        PlayerGameProfile profile = new PlayerGameProfile(uuid, name);
        boolean cape = capeUrl != null && !(capeUrl.isEmpty());

        List<Object> args = new ArrayList<>();
        args.add(System.currentTimeMillis());
        args.add(UUIDTypeAdaptor.fromUUID(uuid));
        args.add(name);
        args.add(skinUrl);

        if (cape) args.add(capeUrl);

        profile.getProperties().add(Skin.from(Base64Coder.encodeString(String.format(cape ? JSON_CAPE : JSON_SKIN, args.toArray(new Object[0])))));

        return profile;
    }

    private static class GameProfileSerializer implements JsonSerializer<PlayerGameProfile>, JsonDeserializer<PlayerGameProfile> {

        @Override
        public PlayerGameProfile deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = (JsonObject) json;
            UUID id = object.has("id") ? (UUID) context.deserialize(object.get("id"), UUID.class) : null;
            String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
            PlayerGameProfile profile = new PlayerGameProfile(id, name);

            if (object.has("properties")) {
                profile.getProperties().addAll(this.deserializeProperties(json, type, context));
            }

            return profile;
        }

        @Override
        public JsonElement serialize(PlayerGameProfile profile, Type type, JsonSerializationContext context) {
            JsonObject result = new JsonObject();

            if (profile.getUuid() != null) result.add("id", context.serialize(profile.getUuid()));

            if (profile.getName() != null) result.addProperty("name", profile.getName());

            if (!(profile.getProperties().isEmpty()))
                result.add("properties", context.serialize(profile.getProperties()));

            return result;
        }

        private List<Property> deserializeProperties(JsonElement json, Type type, JsonDeserializationContext context) {
            List<Property> result = new ArrayList<>();
            if (json instanceof JsonObject) {
                JsonObject object = (JsonObject) json;
                for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                    if (!(entry.getValue() instanceof JsonArray)) continue;

                    for (JsonElement element : ((JsonArray) entry.getValue())) {
                        result.add(Property.builder().name(entry.getKey()).value(element.getAsString()).build());
                    }
                }
            } else if (json instanceof JsonArray) {
                for (JsonElement element : ((JsonArray) json)) {
                    if (element instanceof JsonObject) {
                        JsonObject object = (JsonObject) element;
                        String name = object.getAsJsonPrimitive("name").getAsString();
                        String value = object.getAsJsonPrimitive("value").getAsString();
                        if (object.has("signature")) {
                            result.add(Property.builder().name(name).value(value).signature(object.getAsJsonPrimitive("signature").getAsString()).build());
                        } else {
                            result.add(Property.builder().name(name).value(value).build());
                        }
                    }
                }
            }
            return result;
        }

    }

}
