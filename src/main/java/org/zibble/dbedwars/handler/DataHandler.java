package org.zibble.dbedwars.handler;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.bridge.*;
import org.zibble.dbedwars.database.data.ArenaHistory;
import org.zibble.dbedwars.database.data.PlayerStats;
import org.zibble.dbedwars.database.data.QuickBuy;
import org.zibble.dbedwars.database.data.QuickBuyData;
import org.zibble.dbedwars.database.data.table.DataTables;
import org.zibble.dbedwars.io.ExternalLibrary;
import org.zibble.dbedwars.utils.DatabaseUtil;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataHandler {

    private final DBedwars plugin;
    private DatabaseBridge database;

    private final Map<UUID, PlayerStats> statsData = new ConcurrentHashMap<>();
    private final Map<UUID, QuickBuy> quickBuyData = new ConcurrentHashMap<>();

    public DataHandler(DBedwars plugin) {
        this.plugin = plugin;
    }

    public void initDatabase() {
        DatabaseType type;
        if (this.plugin.getConfigHandler().getDatabase().getDatabase() != null)
            type = this.plugin.getConfigHandler().getDatabase().getDatabase();
        else type = DatabaseType.SQLite;


        if (type != DatabaseType.MongoDB) {
            ExternalLibrary.JOOQ.loadSafely();
            ExternalLibrary.HIKARI_CP.loadSafely();
        }

        if (type == DatabaseType.MongoDB) {
            ExternalLibrary.MONGO_DATABASE.loadSafely();
            this.database = new MongoDBBridge(this.plugin.getConfigHandler().getDatabase().getMongoDB());
        } else if (type == DatabaseType.MYSQL) {
            this.database = new MySQLBridge(this.plugin.getConfigHandler().getDatabase().getMySQL());
        } else if (type == DatabaseType.H2) {
            ExternalLibrary.H2_DATABASE.loadSafely();
            this.database = new H2DatabaseBridge();
        } else if (type == DatabaseType.PostGreSQL) {
            ExternalLibrary.POSTGRESQL_DATABASE.loadSafely();
            this.database = new PostGreSqlBridge(this.plugin.getConfigHandler().getDatabase().getMySQL());
        } else {
            ExternalLibrary.SQLITE_DATABASE.loadSafely();
            this.database = new SQLiteBridge();
        }

        this.database.init();
    }

    public ActionFuture<Void> tryInsertNew(UUID uuid, String name) {
        return this.database.insertNewPlayerData(DataTables.STATS, new PlayerStats(uuid, name))
                .thenCompose(v -> this.database.insertNewPlayerData(DataTables.QUICK_BUY, new QuickBuy(uuid, name, new QuickBuyData())))
                .thenApply(v -> null);
    }

    public ActionFuture<Void> loadAll(UUID uuid) {
        return this.database.requestPlayerData(DataTables.STATS, uuid)
                .thenAccept(s -> this.statsData.put(uuid, s))
                .thenCompose(v -> this.database.requestPlayerData(DataTables.QUICK_BUY, uuid))
                .thenAccept(q -> this.quickBuyData.put(uuid, q));
    }

    public ActionFuture<Void> saveAll(UUID uuid) {
        PlayerStats stat = this.statsData.remove(uuid);
        if (stat == null) return ActionFuture.completedFuture(null);
        return this.database.updatePlayerData(DataTables.STATS, stat)
                .thenCompose(v -> {
                    QuickBuy quickBuy = this.quickBuyData.remove(uuid);
                    if (quickBuy == null) return ActionFuture.completedFuture(null);
                    return this.database.updatePlayerData(DataTables.QUICK_BUY, quickBuy);
                })
                .thenApply(v -> null);
    }

    public PlayerStats getPlayerStats(UUID uuid) {
        return this.statsData.get(uuid);
    }

    public QuickBuy getQuickBuy(UUID uuid) {
        return this.quickBuyData.get(uuid);
    }

    public ActionFuture<PlayerStats> requestOfflinePlayerStats(UUID uuid) {
        return this.database.requestPlayerData(DataTables.STATS, uuid);
    }

    public ActionFuture<QuickBuy> requestOfflineQuickBuy(UUID uuid) {
        return this.database.requestPlayerData(DataTables.QUICK_BUY, uuid);
    }

    public void saveArenaHistory(Arena arena, Color winner) {
        ArenaHistory history = DatabaseUtil.createHistory(arena, winner);
        this.database.insertArenaHistory(history);
    }

    public DatabaseBridge getBridge() {
        return database;
    }

}
