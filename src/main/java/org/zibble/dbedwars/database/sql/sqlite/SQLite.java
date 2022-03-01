package org.zibble.dbedwars.database.sql.sqlite;

import org.zibble.dbedwars.database.DatabaseType;
import org.zibble.dbedwars.database.sql.SQLDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

/** Class for interacting with a SQLite database. */
public class SQLite extends SQLDatabase {

    /** The JDBC driver class. */
    private static final String DRIVER_CLASS = "org.sqlite.JDBC";

    private final File db;
    private final boolean reconnect;

    private Connection connection;
    private int lost_connections;

    /**
     * Constructs the SQLite database.
     *
     * <p><strong>Note:</strong> Database file should end with {@code .db} extension.
     *
     * <p>
     *
     * @param db Database file
     * @param reconnect <strong>{@code true}</strong> to auto reconnect
     */
    public SQLite(File db, boolean reconnect) {
        super(DatabaseType.SQLite);

        if (db == null) throw new IllegalArgumentException("The database file cannot be null!");

        this.db = db;
        this.reconnect = reconnect;
    }

    /**
     * Constructs the SQLite database. Auto-reconnect is set {@code true}.
     *
     * <p><strong>Note:</strong> Database file should end with {@code .db} extension.
     *
     * <p>
     *
     * @param db Database file
     */
    public SQLite(File db) {
        this(db, true);
    }

    /**
     * Gets whether connected to SQLite.
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
     *
     *
     * <h1>Returns:</h1>
     *
     * <ul>
     *   <li>The current connection if connected to SQLite:
     *   <li>The new connection if and only if:
     *       <ul>
     *         <li>It wasn't connected.
     *         <li>The auto-reconnection is enabled.
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
     * @return Connection or null if not connected.
     * @throws IOException when the given database file cannot be created
     * @throws SQLTimeoutException when the driver has determined that the timeout value has been
     *     exceeded and has at least tried to cancel the current database connection attempt.
     * @throws IllegalStateException if the JDBC drivers is unavailable or the file is not s sqlite
     *     database file
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Connection getConnection() throws IOException, SQLTimeoutException, IllegalStateException, SQLException {
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
     * Starts the connection with SQLite.
     *
     * <p>
     *
     * @throws IOException when the given database file cannot be created
     * @throws IllegalStateException if the JDBC drivers is unavailable or the file is not s sqlite
     *     database file
     * @throws SQLException if a database access error occurs.
     * @throws SQLTimeoutException when the driver has determined that the timeout has been exceeded
     *     and has at least tried to cancel the current database connection attempt.
     */
    @Override
    public synchronized void connect() throws IOException, IllegalStateException, SQLException, SQLTimeoutException {
        if (!this.db.getName().endsWith(".db"))
            throw new IllegalStateException("The database file should have '.db' extension.");

        if (!this.db.getParentFile().exists()) this.db.getParentFile().mkdirs();

        if (!this.db.exists()) this.db.createNewFile();
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(
                    "Could not connect to SQLite! The JDBC driver is unavailable!");
        }
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.db.getAbsolutePath());
    }

    /**
     * Closes the connection with SQLite.
     *
     * <p>
     *
     * @throws IllegalStateException if currently not connected, the connection should be checked
     *     before calling this: {@link #isConnected()}.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public void disconnect() throws SQLException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected!");
        }

        this.connection.close();
        this.connection = null;
    }
}
