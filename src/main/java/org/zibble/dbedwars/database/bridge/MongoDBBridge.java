package org.zibble.dbedwars.database.bridge;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.configuration.configurable.ConfigurableArena;
import org.zibble.dbedwars.configuration.configurable.ConfigurableDatabase;
import org.zibble.dbedwars.database.data.*;
import org.zibble.dbedwars.database.data.table.DataTable;
import org.zibble.dbedwars.database.mongo.MongoDB;
import org.zibble.dbedwars.database.mongo.codec.QuickBuyDataCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MongoDBBridge implements DatabaseBridge {

    private final MongoDB database;
    private final Map<String, MongoCollection<?>> collections;
    private boolean initialized;

    public MongoDBBridge(ConfigurableDatabase.ConfigurableMongoDB cfg) {
        this.database = new MongoDB(cfg.getHost(), cfg.getPort(), cfg.getDatabaseName(), MongoClientOptions.builder().build(), cfg.getUsername(), cfg.getPassword());
        this.collections = new HashMap<>();
    }

    @Override
    public void init() {
        if (this.initialized) {
            throw new IllegalStateException("Database already initialized!");
        }

        CodecProvider provider = PojoCodecProvider.builder().register(PlayerStats.class, PersistentStat.class, QuickBuy.class, ArenaHistory.class).build();
        CodecRegistry registry = CodecRegistries.fromRegistries(CodecRegistries.fromProviders(provider),
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(new QuickBuyDataCodec()));

        this.database.setOptions(MongoClientOptions.builder().codecRegistry(registry).build());

        this.database.connect();
        this.collections.put("player_stats", this.database.getCollection("dbedwars_player_stats", PlayerStats.class));
        this.collections.put("quick_buy", this.database.getCollection("dbedwars_quick_buy", QuickBuy.class));
        for (ConfigurableArena arena : DBedwars.getInstance().getConfigHandler().getArenas()) {
            this.collections.put(arena.getIdentifier(), this.database.getCollection("dbedwars_arena_" + arena.getIdentifier()));
        }
        this.initialized = true;
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
    public boolean isInitialized() {
        return this.initialized;
    }

    public MongoDB getHandle() {
        return this.database;
    }

}
