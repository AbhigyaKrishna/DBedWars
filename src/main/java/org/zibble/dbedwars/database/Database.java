package org.zibble.dbedwars.database;

public abstract class Database {

    private final DatabaseType databaseType;

    public Database(DatabaseType type) {
        this.databaseType = type;
    }

    public abstract boolean isConnected();

    public abstract void connect() throws Exception;

    public abstract void disconnect() throws Exception;

    public DatabaseType getDatabaseType() {
        return this.databaseType;
    }

}
