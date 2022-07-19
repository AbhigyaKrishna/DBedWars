package org.zibble.dbedwars.database.sql.postgresql;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.sql.SQLException;

public class PostGreSQL extends SQLDatabase {

    /**
     * Connection URL format.
     */
    private static final String URL_FORMAT =
            "jdbc:postgresql://"
                    + "%s" // host
                    + ":"
                    + "%d" // port
                    + "/"
                    + "%s" // database
            ;

    private static final String DRIVER_CLASS = "org.postgresql.Driver";

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String params;

    public PostGreSQL(String host, int port, String database, String username, String password, String params, HikariConfig config) {
        super(DatabaseType.PostGreSQL, config);

        Preconditions.checkArgument(!Strings.isNullOrEmpty(host), "The host cannot be null or empty!");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(database), "The database cannot be null or empty!");
        Preconditions.checkNotNull(username, "The username cannot be null!");
        Preconditions.checkNotNull(password, "The password cannot be null!");

        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.params = params;
    }

    public PostGreSQL(String host, int port, String database, String username, String password, String params) {
        this(host, port, database, username, password, params, new HikariConfig());
    }

    @Override
    public synchronized void connect() throws IllegalStateException, SQLException {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not connect to PostGreSQL! The JDBC driver is unavailable!");
        }

        if (this.dataSource == null) {
            this.config.setDriverClassName(DRIVER_CLASS);
            this.config.setJdbcUrl(String.format(URL_FORMAT, host, port, database) + this.params);
            this.config.setUsername(username);
            this.config.setPassword(password);
            this.dataSource = new HikariDataSource(this.config);
        }

        this.connection = this.dataSource.getConnection();
    }

}
