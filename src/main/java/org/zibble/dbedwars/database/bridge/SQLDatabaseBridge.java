package org.zibble.dbedwars.database.bridge;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariConfig;
import org.jooq.SQLDialect;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.data.ArenaHistory;
import org.zibble.dbedwars.database.data.PlayerDataCache;
import org.zibble.dbedwars.database.data.PlayerStats;
import org.zibble.dbedwars.database.data.QuickBuy;
import org.zibble.dbedwars.database.data.table.DataTable;
import org.zibble.dbedwars.database.hikaricp.HikariConfigBuilder;
import org.zibble.dbedwars.database.jooq.JooqContext;
import org.zibble.dbedwars.database.jooq.records.ArenaHistoryRecord;
import org.zibble.dbedwars.database.jooq.records.PlayerStatRecord;
import org.zibble.dbedwars.database.jooq.records.QuickBuyRecord;
import org.zibble.dbedwars.database.jooq.sql.CreateTableSQL;
import org.zibble.dbedwars.database.jooq.sql.FetchDataSQL;
import org.zibble.dbedwars.database.jooq.sql.InsertDataSQL;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;
import org.zibble.dbedwars.database.jooq.tables.QuickBuyTable;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

@SuppressWarnings("unchecked")
public abstract class SQLDatabaseBridge implements DatabaseBridge {

    private final SQLDatabase database;
    private final JooqContext context;
    private boolean initialized;

    public SQLDatabaseBridge(SQLDatabase database) {
        this.database = database;
        this.context = new JooqContext(this.getDialect(this.database.getDatabaseType()));
    }

    protected static HikariConfig createHikariConfig(DatabaseType type) {
        HikariConfigBuilder builder = new HikariConfigBuilder();

        if (type != DatabaseType.H2 && type != DatabaseType.SQLite) {
            builder.addProperty("keepaliveTime", "30000ms")
                    .addProperty("useLocalSessionState", "true")
                    .addProperty("rewriteBatchedStatements", "true")
                    .addProperty("cachePrepStmts", "true")
                    .addProperty("prepStmtCacheSize", "250")
                    .addProperty("prepStmtCacheSqlLimit", "2048")
                    .addProperty("useServerPrepStmts", "true")
                    .addProperty("cacheServerConfiguration", "true")
                    .addProperty("elideSetAutoCommits", "true")
                    .addProperty("cacheResultSetMetadata", "true")
                    .addProperty("maintainTimeStats", "false");
        }

        return builder.build();
    }

    @Override
    public void init() {
        Preconditions.checkArgument(!this.initialized, "DatabaseBridge is already initialized");

        try {
            this.database.connect();
            CreateTableSQL createTable = new CreateTableSQL(this.context.createContext(this.database.getConnection()));
            createTable.createStatsTable().executeAsync();
            createTable.createQuickBuyTable().executeAsync();
            createTable.createArenaTable().executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.initialized = true;
    }

    @Override
    public void disconnect() {
        try {
            this.database.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends PlayerDataCache> ActionFuture<T> requestPlayerData(DataTable<T> dataTable, UUID uuid) {
        try {
            FetchDataSQL fetchData = new FetchDataSQL(this.context.createContext(this.database.getConnection()));
            switch (dataTable.database()) {
                case "player_stats":
                    return (ActionFuture<T>) fetchData.fetchPlayerStat(uuid).executeAsync();
                case "quick_buy":
                    return (ActionFuture<T>) fetchData.fetchQuickBuy(uuid).executeAsync();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ActionFuture.completedFuture(null);
    }

    @Override
    public <T extends PlayerDataCache> ActionFuture<Boolean> insertNewPlayerData(DataTable<T> dataTable, T dataCache) {
        try {
            InsertDataSQL insertData = new InsertDataSQL(this.context.createContext(this.database.getConnection()));
            switch (dataTable.database()) {
                case "player_stats":
                    return insertData.insertNewPlayerStat(dataCache.getUuid(), dataCache.getName()).executeAsync();
                case "quick_buy":
                    return insertData.insertNewQuickBuy(dataCache.getUuid(), dataCache.getName()).executeAsync();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ActionFuture.completedFuture(false);
    }

    @Override
    public <T extends PlayerDataCache> ActionFuture<Boolean> updatePlayerData(DataTable<T> dataTable, T dataCache) {
        switch (dataTable.database()) {
            case "player_stats":
                return ActionFuture.supplyAsync(() -> {
                    try {
                        PlayerStats stats = (PlayerStats) dataCache;
                        return this.context.createContext(this.database.getConnection())
                                .update(PlayerStatTable.PLAYER_STAT)
                                .set(PlayerStatRecord.fromDataCache(stats))
                                .where(PlayerStatTable.PLAYER_STAT.UUID.eq(stats.getUuid()))
                                .execute() > 0;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            case "quick_buy":
                return ActionFuture.supplyAsync(() -> {
                    try {
                        QuickBuy quickBuy = (QuickBuy) dataCache;
                        return this.context.createContext(this.database.getConnection())
                                .update(QuickBuyTable.QUICK_BUY)
                                .set(QuickBuyRecord.fromDataCache(quickBuy))
                                .where(QuickBuyTable.QUICK_BUY.UUID.eq(quickBuy.getUuid()))
                                .execute() > 0;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        }

        return ActionFuture.completedFuture(null);
    }

    @Override
    public ActionFuture<Boolean> insertArenaHistory(ArenaHistory history) {
        try {
            InsertDataSQL insertData = new InsertDataSQL(this.context.createContext(this.database.getConnection()));
            return insertData.insertNewArenaHistory(ArenaHistoryRecord.from(history)).executeAsync();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ActionFuture.completedFuture(false);
    }

    @Override
    public Collection<ArenaHistory> getArenaHistory(String arenaId) {
        return null;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    public SQLDatabase getDatabase() {
        return this.database;
    }

    private SQLDialect getDialect(DatabaseType type) {
        switch (type) {
            case H2:
                return SQLDialect.H2;
            case SQLite:
                return SQLDialect.SQLITE;
            case PostGreSQL:
                return SQLDialect.POSTGRES;
            default:
                return SQLDialect.MYSQL;
        }
    }

}
