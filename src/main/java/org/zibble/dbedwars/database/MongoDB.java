package org.zibble.dbedwars.database;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.pepedevs.radium.database.Database;
import com.pepedevs.radium.database.mongo.MongoDocument;
import com.pepedevs.radium.utils.json.Json;
import com.pepedevs.radium.utils.reflection.general.FieldReflection;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.cache.DataCache;
import org.zibble.dbedwars.utils.JSONBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MongoDB extends DatabaseBridge {

    private final com.pepedevs.radium.database.mongo.MongoDB db;

    public MongoDB(DBedwars plugin, String host, int port, String databaseName) {
        super(plugin);
        this.db = new com.pepedevs.radium.database.mongo.MongoDB(host, port, databaseName);
    }

    @Override
    public void init() {
        this.db.connect();
    }

    @Override
    public void disconnect() {
        this.db.disconnect();
    }

    @Override
    public Json getData(String key, Object gate, String database) {
        DBCollection collection = this.db.getCollection(database);
        MongoDocument document = new MongoDocument(collection);
        Set<DBObject> objects = document.getDocument(key, gate);
        JSONBuilder builder = new JSONBuilder();
        int i = 0;
        boolean found = false;
        for (DBObject object : objects) {
            found = true;
            Json json = Json.loadFromString(this.getGson().toJson(object));
            builder.add(String.valueOf(i), json.getHandle());
            i++;
        }

        return found ? builder.getRawJson() : null;
    }

    @Override
    public List<DataCache> getAsDataCache(String key, Object gate, String database) {
        List<DataCache> caches = new ArrayList<>();

        Map.Entry<List<String>, Class<? extends DataCache>> entry =
                DATABASE_NAME_COLUMNS.getOrDefault(database, null);
        if (entry == null) return caches;

        DBCollection collection = this.db.getCollection(database);
        MongoDocument document = new MongoDocument(collection);
        Set<DBObject> objects = document.getDocument(key, gate);

        for (DBObject object : objects) {
            Json json = Json.loadFromString(this.getGson().toJson(object));
            caches.add(this.getGson().fromJson(json.toString(), entry.getValue()));
        }

        return caches;
    }

    @Override
    public boolean saveData(DataCache data, String database, boolean update) {
        Json json = Json.loadFromString(this.getGson().toJson(data));

        Map.Entry<List<String>, Class<? extends DataCache>> entry =
                DATABASE_NAME_COLUMNS.getOrDefault(database, null);
        if (entry == null) return false;

        DBCollection collection = this.db.getCollection(database);
        MongoDocument document = new MongoDocument(collection);

        if (!update) {
            document.insert("UUID", json.getAsJsonPrimitive("UUID").getAsString());
        }

        for (String col : entry.getKey()) {
            if (col.equals("UUID")) continue;

            try {
                document.update(
                        "UUID",
                        json.getAsJsonPrimitive("UUID").getAsString(),
                        col,
                        FieldReflection.getValue(json.getAsJsonPrimitive(col), "value"));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public Database getHandle() {
        return this.db;
    }
}