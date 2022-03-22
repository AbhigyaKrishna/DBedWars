package org.zibble.dbedwars.database.bridge;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.IOUtils;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.database.data.PlayerDataCache;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;
import org.zibble.dbedwars.database.data.table.DataTable;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public abstract class SQLDatabaseBridge implements DatabaseBridge {

    private Cache<String, String> sqlCache = CacheBuilder.newBuilder().maximumSize(10).build();
    private final SQLDatabase database;
    private boolean initialized;

    public SQLDatabaseBridge(SQLDatabase database) {
        this.database = database;
    }

    @Override
    public void init() {
        if (this.initialized) {
            throw new IllegalStateException("Database already initialized!");
        }

        try {
            this.database.connect();
            this.querySQLFile("stats_database_init", this.database::executeAsync);
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
        String sql = "SELECT * FROM `" + dataTable.database() + "` WHERE `uuid` = '" + uuid.toString() + "'";
        return this.database.queryAsync(sql, resultSet -> {
            if (resultSet.next()) {
                SQLDataReader reader = new SQLDataReader(resultSet);
                T data = dataTable.newInstance();
                try {
                    data.load(reader);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return data;
            }
            return null;
        });
    }

    @Override
    public <T extends PlayerDataCache> ActionFuture<Boolean> insertNewPlayerData(DataTable<T> dataTable, T dataCache) {
        SQLDataWriter writer = new SQLDataWriter(SQLDataWriter.WriteOperation.insert(dataTable.database(), null));
        try {
            dataCache.save(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.database.updateAsync(writer.getData().complete()).thenApply(i -> i > 0);
    }

    @Override
    public <T extends PlayerDataCache> ActionFuture<Boolean> updatePlayerData(DataTable<T> dataTable, T dataCache) {
        SQLDataWriter writer = new SQLDataWriter(SQLDataWriter.WriteOperation.update(dataTable.database(), dataCache.getUuid(), (key, value) -> key.equals("uuid")));
        try {
            dataCache.save(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.database.updateAsync(writer.getData().complete()).thenApply(i -> i > 0);
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    public SQLDatabase getDatabase() {
        return this.database;
    }

    protected void querySQLFile(String fileName, Consumer<String> consumer) throws IOException {
        String s;
        if ((s = this.sqlCache.getIfPresent(fileName)) == null) {
            try (InputStream sql = DBedwars.getInstance().getResource("sql/" + fileName + ".sql")) {
                s = IOUtils.toString(sql, StandardCharsets.UTF_8);
                this.sqlCache.put(fileName, s);
            }
        }
        consumer.accept(s);
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

}
