package org.zibble.dbedwars.database.sql.sqlite;

import com.zaxxer.hikari.HikariConfig;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class for interacting with a SQLite database.
 */
public class SQLite extends SQLDatabase {

    /**
     * The JDBC driver class.
     */
    private static final String DRIVER_CLASS = "org.sqlite.JDBC";

    private final File db;

    public SQLite(File db) {
        super(DatabaseType.SQLite, new HikariConfig());

        if (db == null) throw new IllegalArgumentException("The database file cannot be null!");

        this.db = db;
    }

    @Override
    public synchronized void connect() throws IOException, IllegalStateException, SQLException {
        if (!this.db.getName().endsWith(".db"))
            throw new IllegalStateException("The database file should have '.db' extension.");

        if (!this.db.getParentFile().exists()) this.db.getParentFile().mkdirs();

        if (!this.db.exists()) this.db.createNewFile();
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not connect to SQLite! The JDBC driver is unavailable!");
        }

        this.connection = this.getConnection();
    }

    @Override
    public Connection getConnection() throws IllegalStateException, SQLException {
        return this.connection = this.isConnected() ? this.connection : DriverManager.getConnection("jdbc:sqlite:" + this.db);
    }

    @Override
    public void disconnect() throws SQLException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected!");
        }

        this.connection.close();
        this.connection = null;
    }

}
