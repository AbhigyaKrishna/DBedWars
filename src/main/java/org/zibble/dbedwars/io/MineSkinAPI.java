package org.zibble.dbedwars.io;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zibble.dbedwars.api.objects.profile.Skin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MineSkinAPI {

    private static final String URL_FORMAT = "https://api.mineskin.org/get/uuid/%s";

    private static final MineSkinAPI INSTANCE = new MineSkinAPI();

    private final Cache<String, Skin> cache = CacheBuilder.newBuilder().expireAfterWrite(120, TimeUnit.MINUTES).build();

    private MineSkinAPI() {
    }

    public Skin getSkin(String id) {
        return this.getSkin(id, false);
    }

    public Skin getSkin(String id, boolean forceNew) {
        Skin skin = this.cache.getIfPresent(id);
        if (!forceNew && skin != null) {
            return skin;
        } else {

            try {
                // Open http connection
                HttpURLConnection textureConnection = (HttpURLConnection) new URL(String.format(URL_FORMAT, id)).openConnection();
                textureConnection.setRequestMethod("GET");
                textureConnection.setReadTimeout(5000);

                if (textureConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // Parse response
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(textureConnection.getInputStream()))) {
                        JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
                        JsonObject texture = jsonObject.get("data").getAsJsonObject().get("texture").getAsJsonObject();

                        skin = Skin.from(texture.get("value").getAsString());
                        if (texture.has("signature")) {
                            skin.setSignature(texture.get("signature").getAsString());
                        }

                        this.cache.put(id, skin);
                    }

                    textureConnection.disconnect();

                    return skin;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Skin.empty();
    }

    public static MineSkinAPI getInstance() {
        return INSTANCE;
    }

}
