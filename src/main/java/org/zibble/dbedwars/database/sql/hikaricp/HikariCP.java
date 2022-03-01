package org.zibble.dbedwars.database.sql.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang.Validate;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

/** Class for interacting with a Hikari database. */
public class HikariCP extends SQLDatabase {

    /** The JDBC driver class. */
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private final HikariConfig config;
    private final HikariDataSource dataSource;
    private final boolean reconnect;

    private Connection connection;
    private int lost_connections;

    /**
     * Constructs the HikariCP database.
     *
     * <p>
     *
     * @param config HikariConfig for connection
     * @param dataSource DataSource for given config
     * @param reconnect <strong>{@code true}</strong> to auto reconnect
     */
    public HikariCP(HikariConfig config, HikariDataSource dataSource, boolean reconnect) {
        super(DatabaseType.HikariCP);

        Validate.notNull(config, "HikariConfig cannot be null!");
        Validate.notNull(dataSource, "HikariDataSource cannot be null!");

        this.config = config;
        this.dataSource = dataSource;
        this.reconnect = reconnect;
    }

    /**
     * Constructs the HikariCP database.
     *
     * <p>
     *
     * @param config HikariConfig for connection
     * @param dataSource DataSource for given config
     */
    public HikariCP(HikariConfig config, HikariDataSource dataSource) {
        this(config, dataSource, true);
    }

    /**
     * Constructs the HikariCP database.
     *
     * <p>
     *
     * @param builder {@link HikariClientBuilder}
     */
    public HikariCP(HikariClientBuilder builder) {
        this(builder.getConfig(), new HikariDataSource(builder.getConfig()), builder.isReconnect());
    }

    /**
     * Gets whether connected to HikariCP.
     *
     * <p>
     *
     * @return true if connected.
     */
    @Override
    public boolean isConnected() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Starts the connection with HikariCP.
     *
     * <p>
     *
     * @throws IllegalStateException if the JDBC drivers is unavailable.
     * @throws SQLException if a database access error occurs.
     * @throws SQLTimeoutException when the driver has determined that the timeout has been exceeded
     *     and has at least tried to cancel the current database connection attempt.
     */
    @Override
    public synchronized void connect() throws IllegalStateException, SQLException {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(
                    "Could not connect to HikariCP! The JDBC connection driver is unavailable!");
        }

        this.connection = dataSource.getConnection();
    }

    /**
     * Closes the connection with HikariCP.
     *
     * <p>
     *
     * @throws IllegalStateException if currently not connected, the connection should be checked
     *     before calling this: {@link #isConnected()}.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void disconnect() throws SQLException, IllegalStateException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected!");
        }

        this.connection.close();
        this.connection = null;
    }

    /**
     *
     *
     * <h1>Returns:</h1>
     *
     * <ul>
     *   <li>The current connection if connected to HikariCP:
     *   <li>The new connection if and only if:
     *       <ul>
     *         <li>It wasn't connected.
     *         <li>The auto-reconnection is enabled.
     *         <li>The attempt to get connection was successfully.
     *       </ul>
     *   <li><strong>{@code null}</strong> if:
     *       <ul>
     *         <li>It wasn't connected and the auto-reconnection is disabled.
     *         <li>The auto-reconnection is enabled but the attempt to get connection was
     *             unsuccessfully.
     *       </ul>
     * </ul>
     *
     * <p>
     *
     * @return Connection or null if not connected
     * @throws SQLTimeoutException when the driver has determined that the timeout value has been
     *     exceeded and has at least tried to cancel the current database connection attempt.
     * @throws IllegalStateException if the JDBC drivers is unavailable
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Connection getConnection() throws IllegalStateException, SQLException {
        if (!isConnected() && reconnect) {
            this.lost_connections++;
            this.connect();
        }
        return this.isConnected() ? this.connection : null;
    }

    /**
     * The times the connection was lost.
     *
     * <p>
     *
     * @return Times the connection was lost, or <strong>{@code -1}</strong> if the
     *     auto-reconnection is disabled.
     */
    @Override
    public int getLostConnections() {
        return reconnect ? lost_connections : -1;
    }

    /**
     * Get {@link HikariConfig}.
     *
     * <p>
     *
     * @return Current HikariConfig
     */
    public HikariConfig getConfig() {
        return config;
    }

    /**
     * Get {@link HikariDataSource}.
     *
     * <p>
     *
     * @return Datasource for current HikariConfig
     */
    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
