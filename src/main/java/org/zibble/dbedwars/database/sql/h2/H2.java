package org.zibble.dbedwars.database.sql.h2;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class H2 extends SQLDatabase {

    /**
     * The JDBC driver class.
     */
    private static final String DRIVER_CLASS = "org.h2.jdbcx.JdbcDataSource";

    private final File db;

    public H2(File db, HikariConfig config) {
        super(DatabaseType.H2, config);
        Preconditions.checkNotNull(db, "The database file cannot be null!");

        this.db = db;
    }

    public H2(File db) {
        this(db, new HikariConfig());
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
            throw new IllegalStateException("Could not connect to H2! The H2 driver is unavailable!");
        }

        if (this.dataSource == null) {
            this.config.setDriverClassName(DRIVER_CLASS);
            this.config.setJdbcUrl("jdbc:h2:" + this.db.getPath());
            this.dataSource = new HikariDataSource(this.config);
        }

        this.connection = this.dataSource.getConnection();
    }

}
