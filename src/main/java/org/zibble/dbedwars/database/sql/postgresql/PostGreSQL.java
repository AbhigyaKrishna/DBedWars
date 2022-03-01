package org.zibble.dbedwars.database.sql.postgresql;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

public class PostGreSQL extends SQLDatabase {

    /** Connection URL format. */
    private static final String URL_FORMAT =
            "jdbc:postgresql://"
                    + "%s" // host
                    + ":"
                    + "%d" // port
                    + "/"
                    + "%s" // database
                    + "?autoReconnect="
                    + "%s" // auto reconnect
                    + "&"
                    + "useSSL="
                    + "%s" // use ssl
            ;

    private static final String DRIVER_CLASS = "org.postgresql.Driver";

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final boolean reconnect;
    private final boolean ssl;

    private Connection connection;
    private int lost_connections;

    /**
     * Constructs the PostGreSQL database.
     *
     * <p>
     *
     * @param host Host name
     * @param port Port number
     * @param database Database name
     * @param username User name
     * @param password User password
     * @param reconnect <strong>{@code true}</strong> to auto reconnect
     * @param ssl <strong>{@code true}</strong> to use SSL
     */
    public PostGreSQL(String host, int port, String database, String username, String password, boolean reconnect, boolean ssl) {
        super(DatabaseType.PostGreSQL);

        Validate.isTrue(!StringUtils.isEmpty(host), "The host cannot be null or empty!");
        Validate.isTrue(!StringUtils.isEmpty(database), "The database cannot be null or empty!");
        Validate.notNull(username, "The username cannot be null!");
        Validate.notNull(password, "The password cannot be null!");

        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.reconnect = reconnect;
        this.ssl = ssl;
    }

    /**
     * Constructs the PostGreSQL database.
     *
     * <p>
     *
     * @param host Host name
     * @param port Port number
     * @param database Database name
     * @param username User name
     * @param password User password
     * @param reconnect <strong>{@code true}</strong> to auto reconnect
     */
    public PostGreSQL(String host, int port, String database, String username, String password, boolean reconnect) {
        this(host, port, database, username, password, reconnect, true);
    }

    /**
     * Gets whether connected to PostGreSQL.
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
     * Starts the connection with PostGreSQL.
     *
     * <p>
     *
     * @throws IllegalStateException if the JDBC drivers is unavailable.
     * @throws SQLException if a database access error occurs.
     * @throws SQLTimeoutException when the driver has determined that the timeout has been exceeded
     *     and has at least tried to cancel the current database connection attempt.
     */
    @Override
    public synchronized void connect() throws IllegalStateException, SQLException, SQLTimeoutException {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(
                    "Could not connect to PostGreSQL! The JDBC driver is unavailable!");
        }

        this.connection = DriverManager.getConnection(String.format(URL_FORMAT, host, port, database, reconnect, ssl), username, password);
    }

    /**
     * Closes the connection with PostGreSQL.
     *
     * <p>
     *
     * @throws IllegalStateException if currently not connected, the connection should be checked
     *     before calling this: {@link #isConnected()}.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void disconnect() throws SQLException {
        if (!this.isConnected()) {
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
     *   <li>The current connection if connected to PostGreSQL:
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
    public Connection getConnection() throws SQLTimeoutException, IllegalStateException, SQLException {
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
}
