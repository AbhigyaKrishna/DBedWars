package org.zibble.dbedwars.database.sql.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;

/**
 * Class for interacting with a MySQL database.
 */
public class MySQL extends SQLDatabase {

    /**
     * Connection URL format.
     */
    private static final String URL_FORMAT =
            "jdbc:mysql://"
                    + "%s" // host
                    + ":"
                    + "%d" // port
                    + "/"
                    + "%s" // database
            ;

    /**
     * The JDBC driver class.
     */
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String LEGACY_DRIVER_CLASS = "com.mysql.jdbc.Driver";

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String params;

    public MySQL(String host, int port, String database, String username, String password, String params, HikariConfig config) {
        super(DatabaseType.MYSQL, config);

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

    /**
     * Constructs the MySQL database.
     *
     * <p>
     *
     * @param host     Host name
     * @param port     Port number
     * @param database Database name
     * @param username User name
     * @param password User password
     */
    public MySQL(String host, int port, String database, String username, String password, String params) {
        this(host, port, database, username, password, params, new HikariConfig());
    }

    /**
     * Starts the connection with MySQL.
     *
     * <p>
     *
     * @throws IllegalStateException if the JDBC drivers is unavailable.
     * @throws SQLException          if a database access error occurs.
     * @throws SQLTimeoutException   when the driver has determined that the timeout has been exceeded
     *                               and has at least tried to cancel the current database connection attempt.
     */
    @Override
    public synchronized void connect() throws IllegalStateException, SQLException, SQLTimeoutException {
        String driverClass = DRIVER_CLASS;

        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            try {
                Class.forName(LEGACY_DRIVER_CLASS);
                driverClass = LEGACY_DRIVER_CLASS;
            } catch (ClassNotFoundException ex2) {
                throw new IllegalStateException("Could not connect to MySQL! The JDBC driver is unavailable!");
            }
        }

        if (this.dataSource == null) {
            this.config.setDataSourceClassName(driverClass);
            this.config.setJdbcUrl(String.format(URL_FORMAT, host, port, database) + this.params);
            this.config.setUsername(username);
            this.config.setPassword(password);
            this.dataSource = new HikariDataSource(this.config);
        }

        this.connection = this.dataSource.getConnection();
    }

}
