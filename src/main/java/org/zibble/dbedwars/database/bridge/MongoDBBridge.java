package org.zibble.dbedwars.database.bridge;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.database.Database;
import org.zibble.dbedwars.database.data.table.DataTable;
import org.zibble.dbedwars.database.data.PlayerStats;
import org.zibble.dbedwars.database.data.PlayerDataCache;
import org.zibble.dbedwars.database.mongo.MongoDB;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MongoDBBridge implements DatabaseBridge {

    private final MongoDB database;
    private final Map<String, MongoCollection<?>> collections;

    public MongoDBBridge(String host, int port, String databaseName, String username, String password) {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.database = new MongoDB(host, port, databaseName, MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build(), username, password);
        this.collections = new HashMap<>();
    }

    @Override
    public void init() {
        this.database.connect();
        this.collections.put("player_stats", this.database.getCollection("player_stats", PlayerStats.class));
    }

    @Override
    public void disconnect() {
        this.database.disconnect();
    }

    @Override
    public <T extends PlayerDataCache> ActionFuture<T> requestPlayerData(DataTable<T> dataTable, UUID uuid) {
        MongoCollection<T> collection = (MongoCollection<T>) this.collections.get(dataTable.database());
        return ActionFuture.supplyAsync(() -> collection.find(Filters.eq("uuid", uuid)).first());
    }

    @Override
    public <T extends PlayerDataCache> ActionFuture<Boolean> insertNewPlayerData(DataTable<T> dataTable, T dataCache) {
        MongoCollection<T> collection = (MongoCollection<T>) this.collections.get(dataTable.database());
        return ActionFuture.supplyAsync(() -> {
            collection.insertOne(dataCache);
            return true;
        });
    }

    @Override
    public <T extends PlayerDataCache> ActionFuture<Boolean> updatePlayerData(DataTable<T> dataTable, T dataCache) {
        MongoCollection<T> collection = (MongoCollection<T>) this.collections.get(dataTable.database());
        return ActionFuture.supplyAsync(() -> {
            collection.replaceOne(Filters.eq("uuid", dataCache.getUuid()), dataCache);
            return true;
        });
    }

    @Override
    public Database getHandle() {
        return this.database;
    }
}
