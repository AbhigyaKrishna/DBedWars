package org.zibble.dbedwars.database.sql.postgresql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.sql.SQLException;

public class PostGreSQL extends SQLDatabase {

    /** Connection URL format. */
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

        Validate.isTrue(!StringUtils.isEmpty(host), "The host cannot be null or empty!");
        Validate.isTrue(!StringUtils.isEmpty(database), "The database cannot be null or empty!");
        Validate.notNull(username, "The username cannot be null!");
        Validate.notNull(password, "The password cannot be null!");

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
            this.config.setDataSourceClassName(DRIVER_CLASS);
            this.config.setJdbcUrl(String.format(URL_FORMAT, host, port, database) + this.params);
            this.config.setUsername(username);
            this.config.setPassword(password);
            this.dataSource = new HikariDataSource(this.config);
        }

        this.connection = this.dataSource.getConnection();
    }

}
