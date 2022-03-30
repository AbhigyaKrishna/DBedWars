package org.zibble.dbedwars.database.sql.sqlite;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

/** Class for interacting with a SQLite database. */
public class SQLite extends SQLDatabase {

    /** The JDBC driver class. */
    private static final String DRIVER_CLASS = "org.sqlite.JDBC";

    private final File db;

    public SQLite(File db, HikariConfig config) {
        super(DatabaseType.SQLite, config);

        if (db == null) throw new IllegalArgumentException("The database file cannot be null!");

        this.db = db;
    }

    public SQLite(File db) {
        this(db, new HikariConfig());
    }

    @Override
    public synchronized void connect() throws IOException, IllegalStateException, SQLException, SQLTimeoutException {
        if (!this.db.getName().endsWith(".db"))
            throw new IllegalStateException("The database file should have '.db' extension.");

        if (!this.db.getParentFile().exists()) this.db.getParentFile().mkdirs();

        if (!this.db.exists()) this.db.createNewFile();
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not connect to SQLite! The JDBC driver is unavailable!");
        }

        if (this.dataSource == null) {
            this.config.setDataSourceClassName(DRIVER_CLASS);
            this.config.setJdbcUrl("jdbc:sqlite:" + this.db.getAbsolutePath());
            this.dataSource = new HikariDataSource(this.config);
        }

        this.connection = this.dataSource.getConnection();
    }

}
