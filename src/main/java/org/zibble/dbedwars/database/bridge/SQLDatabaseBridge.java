package org.zibble.dbedwars.database.bridge;

import org.jooq.SQLDialect;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.data.PlayerDataCache;
import org.zibble.dbedwars.database.data.PlayerStats;
import org.zibble.dbedwars.database.data.QuickBuy;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;
import org.zibble.dbedwars.database.data.table.DataTable;
import org.zibble.dbedwars.database.jooq.JooqContext;
import org.zibble.dbedwars.database.jooq.records.PlayerStatRecord;
import org.zibble.dbedwars.database.jooq.records.QuickBuyRecord;
import org.zibble.dbedwars.database.jooq.sql.CreateTable;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;
import org.zibble.dbedwars.database.jooq.tables.QuickBuyTable;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.BiPredicate;

@SuppressWarnings("unchecked")
public abstract class SQLDatabaseBridge implements DatabaseBridge {

    private final SQLDatabase database;
    private boolean initialized;
    private final JooqContext context;

    public SQLDatabaseBridge(SQLDatabase database) {
        this.database = database;
        this.context = new JooqContext(this.getDialect(this.database.getDatabaseType()));
    }

    @Override
    public void init() {
        if (this.initialized) {
            throw new IllegalStateException("Database already initialized!");
        }

        try {
            this.database.connect();
            CreateTable createTable = new CreateTable(this.context.createContext(this.database.getConnection()));
            createTable.createStatsTable().executeAsync();
            createTable.createQuickBuyTable().executeAsync();
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
        switch (dataTable.database()) {
            case "player_stats":
                return ActionFuture.supplyAsync(() -> {
                    try {
                        return (T) this.context.createContext(this.database.getConnection())
                                .fetchOne(PlayerStatTable.PLAYER_STAT, PlayerStatTable.PLAYER_STAT.UUID.eq(uuid))
                                .toDataCache();
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            case "quick_buy":
                return ActionFuture.supplyAsync(() -> {
                    try {
                        return (T) this.context.createContext(this.database.getConnection())
                                .fetchOne(QuickBuyTable.QUICK_BUY, QuickBuyTable.QUICK_BUY.UUID.eq(uuid))
                                .toDataCache();
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        }

        return ActionFuture.completedFuture(null);
    }

    @Override
    public <T extends PlayerDataCache> ActionFuture<Boolean> insertNewPlayerData(DataTable<T> dataTable, T dataCache) {
        switch (dataTable.database()) {
            case "player_stats":
                return ActionFuture.supplyAsync(() -> {
                    try {
                        return this.context.createContext(this.database.getConnection())
                                .insertInto(PlayerStatTable.PLAYER_STAT)
                                .set(PlayerStatRecord.newRecord(dataCache.getUuid(), dataCache.getName()))
                                .onDuplicateKeyIgnore()
                                .execute() > 0;
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            case "quick_buy":
                return ActionFuture.supplyAsync(() -> {
                    try {
                        return this.context.createContext(this.database.getConnection())
                                .insertInto(QuickBuyTable.QUICK_BUY)
                                .set(QuickBuyRecord.newRecord(dataCache.getUuid(), dataCache.getName()))
                                .onDuplicateKeyIgnore()
                                .execute() > 0;
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        }

        return ActionFuture.completedFuture(null);
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
                    } catch (SQLException | IOException e) {
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
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        }

        return ActionFuture.completedFuture(null);
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    public SQLDatabase getDatabase() {
        return this.database;
    }

    protected static class SQLDataReader extends DataReader<ResultSet> {

        public SQLDataReader(ResultSet handle) {
            super(handle);
        }

        @Override
        public char readChar(String key) throws SQLException {
            return this.handle.getString(key).charAt(0);
        }

        @Override
        public String readString(String key) throws SQLException {
            return this.handle.getString(key);
        }

        @Override
        public boolean readBoolean(String key) throws SQLException {
            return this.handle.getBoolean(key);
        }

        @Override
        public byte readByte(String key) throws SQLException {
            return this.handle.getByte(key);
        }

        @Override
        public short readShort(String key) throws SQLException {
            return this.handle.getShort(key);
        }

        @Override
        public int readInt(String key) throws SQLException {
            return this.handle.getInt(key);
        }

        @Override
        public long readLong(String key) throws SQLException {
            return this.handle.getLong(key);
        }

        @Override
        public float readFloat(String key) throws SQLException {
            return this.handle.getFloat(key);
        }

        @Override
        public double readDouble(String key) throws SQLException {
            return this.handle.getDouble(key);
        }

        @Override
        public UUID readUUID(String key) throws SQLException {
            return UUID.fromString(this.handle.getString(key));
        }

    }

    protected static class SQLDataWriter extends DataWriter<SQLDataWriter.WriteOperation> {

        public SQLDataWriter(WriteOperation writeOperation) {
            super(writeOperation);
        }

        @Override
        public void writeChar(String key, char value) {
            this.data.write(key, "'" + value + "'");
        }

        @Override
        public void writeString(String key, String value) {
            this.data.write(key, "'" + value + "'");
        }

        @Override
        public void writeBoolean(String key, boolean value) {
            this.data.write(key, value ? "1" : "0");
        }

        @Override
        public void writeByte(String key, byte value) {
            this.data.write(key, String.valueOf(value));
        }

        @Override
        public void writeInt(String key, int value) {
            this.data.write(key, String.valueOf(value));
        }

        @Override
        public void writeLong(String key, long value) {
            this.data.write(key, String.valueOf(value));
        }

        @Override
        public void writeShort(String key, short value) {
            this.data.write(key, String.valueOf(value));
        }

        @Override
        public void writeFloat(String key, float value) {
            this.data.write(key, String.valueOf(value));
        }

        @Override
        public void writeDouble(String key, double value) {
            this.data.write(key, String.valueOf(value));
        }

        @Override
        public void writeUUID(String key, UUID value) {
            this.data.write(key, "'" + value.toString() + "'");
        }

        public interface WriteOperation {

            static WriteOperation insert(String database, BiPredicate<String, String> writePredicate) {
                return new WriteOperation() {
                    final StringBuilder builder = new StringBuilder("INSERT INTO `" + database + "` ");
                    final StringBuilder keys = new StringBuilder("(");
                    final StringBuilder values = new StringBuilder("(");

                    @Override
                    public void write(String key, String value) {
                        if (writePredicate != null && writePredicate.test(key, value))
                            return;

                        keys.append("`").append(key).append("`, ");
                        values.append(values).append(", ");
                    }

                    @Override
                    public String complete() {
                        keys.deleteCharAt(keys.length() - 1)
                                .deleteCharAt(keys.length() - 1)
                                .append(")");
                        values.deleteCharAt(values.length() - 1)
                                .deleteCharAt(values.length() - 1)
                                .append(")");
                        builder.append(keys)
                                .append(" VALUES ")
                                .append(values)
                                .append(";");
                        return builder.toString();
                    }
                };
            }

            static WriteOperation update(String database, UUID uuid, BiPredicate<String, String> writePredicate) {
                return new WriteOperation() {
                    final StringBuilder builder = new StringBuilder("UPDATE `" + database + "` SET ");

                    @Override
                    public void write(String key, String value) {
                        if (writePredicate != null && writePredicate.test(key, value))
                            return;

                        builder.append("`").append(key).append("` = ").append(value).append(", ");
                    }

                    @Override
                    public String complete() {
                        return builder.deleteCharAt(builder.length() - 1)
                                .deleteCharAt(builder.length() - 1)
                                .append(" WHERE `uuid` = '")
                                .append(uuid.toString())
                                .append("';")
                                .toString();
                    }
                };
            }

            void write(String key, String value);

            String complete();

        }

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
