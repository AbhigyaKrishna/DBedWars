package org.zibble.dbedwars.database.sql.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.configLang.StringUtils;
import org.apache.commons.configLang.Validate;

import java.util.Properties;

/** Class for creating Hikari client. */
public class HikariClientBuilder {

    private static final String URL_FORMAT =
            "jdbc:mysql://"
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

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final boolean reconnect;
    private final boolean ssl;

    private HikariConfig config;

    /**
     * Constructs the Hikari Client Builder.
     *
     * <p>
     *
     * @param host Hostname
     * @param username Username
     * @param password Password
     * @param reconnect <strong>{@code true}</strong> to auto reconnect
     */
    public HikariClientBuilder(String host, int port, String database, String username, String password, boolean reconnect, boolean ssl) {
        this.config = new HikariConfig();

        Validate.isTrue(!StringUtils.isEmpty(host), "The host cannot be null or empty!");
        Validate.isTrue(!StringUtils.isEmpty(database), "The database cannot be null or empty!");
        Validate.notNull(username, "Username cannot be null!");
        Validate.notNull(password, "Password cannot be null!");

        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.reconnect = reconnect;
        this.ssl = ssl;

        this.config.setJdbcUrl(String.format(URL_FORMAT, this.host, this.port, this.database, this.reconnect, this.ssl));
        this.config.setUsername(this.username);
        this.config.setPassword(this.password);
    }

    /**
     * Constructs the Hikari Client Builder.
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
    public HikariClientBuilder(String host, int port, String database, String username, String password, boolean reconnect) {
        this(host, port, database, username, password, reconnect, true);
    }

    /**
     * Add a property (name/value pair) that will be used to configure the DataSource Driver.
     *
     * <p>
     *
     * @param key The name of property
     * @param value Value of the property
     * @return This Object, for chaining
     * @see HikariConfig#addDataSourceProperty(String, Object)
     */
    public HikariClientBuilder addProperty(String key, String value) {
        this.config.addDataSourceProperty(key, value);
        return this;
    }

    /**
     * Set the fully qualified class name of the JDBC DataSource that will be used create
     * Connections.
     *
     * <p>
     *
     * @param className The fully qualified name of the JDBC DataSource class
     * @return This Object, for chaining
     * @see HikariConfig#setDataSourceClassName(String)
     */
    public HikariClientBuilder setDataSourceClassName(String className) {
        this.config.setDataSourceClassName(className);
        return this;
    }

    /**
     * Set the provided {@link Properties} to configure the driver.
     *
     * <p>
     *
     * @param property {@link Properties} to set to the driver
     * @return This Object, for chaining
     */
    public HikariClientBuilder setProperty(Properties property) {
        this.config.setDataSourceProperties(property);
        return this;
    }

    /**
     * Set the default auto-commit behaviour of connections in the pool.
     *
     * <p>
     *
     * @param value The desired auto-commit value
     * @return This Object, for chaining
     * @see HikariConfig#setAutoCommit(boolean)
     */
    public HikariClientBuilder setAutoCommit(boolean value) {
        this.config.setAutoCommit(value);
        return this;
    }

    /**
     * Set the maximum number of milliseconds that a client will wait for the connection from the
     * pool.
     *
     * <p>
     *
     * @param timeout The connection timeout in milliseconds
     * @return This Object, for chaining
     * @see HikariConfig#setConnectionTimeout(long)
     */
    public HikariClientBuilder setConnectionTimeout(long timeout) {
        this.config.setConnectionTimeout(timeout);
        return this;
    }

    /**
     * Set the maximum amount a time (in milliseconds) that a connection is allowed to sit idle in
     * the pool.
     *
     * <p>
     *
     * @param timeout The idle timeout in milliseconds
     * @return This Object, for chaining
     * @see HikariConfig#setIdleTimeout(long)
     */
    public HikariClientBuilder setIdleTimeout(long timeout) {
        this.config.setIdleTimeout(timeout);
        return this;
    }

    /**
     * Set the keep alive timeout for the connection in the pool.
     *
     * <p>
     *
     * @param time The interval in milliseconds in which the connection will be tested for aliveness
     * @return This Object, for chaining
     * @see HikariConfig#setKeepaliveTime(long)
     */
    public HikariClientBuilder setKeepAliveTime(long time) {
        this.config.setKeepaliveTime(time);
        return this;
    }

    /**
     * Set the maximum life of the connection in the pool.
     *
     * <p>
     *
     * @param time The maximum connection lifetime in the pool
     * @return This Object, for chaining
     * @see HikariConfig#setMaxLifetime(long)
     */
    public HikariClientBuilder setMaxLifeTime(long time) {
        this.config.setMaxLifetime(time);
        return this;
    }

    /**
     * Set the SQL query to be executed to test the validity of the connection.
     *
     * <p>
     *
     * @param query SQL query string
     * @return This Object, for chaining
     * @see HikariConfig#setConnectionTestQuery(String)
     */
    public HikariClientBuilder setConnectionTestQuery(String query) {
        this.config.setConnectionTestQuery(query);
        return this;
    }

    /**
     * Set the minimum number of idle connections that will be maintained in the pool.
     *
     * <p>
     *
     * @param minIdle The minimum number of idle connections
     * @return This Object, for chaining
     * @see HikariConfig#setMinimumIdle(int)
     */
    public HikariClientBuilder setMinimumIdle(int minIdle) {
        this.config.setMinimumIdle(minIdle);
        return this;
    }

    /**
     * Set the maximum size that the pool is allowed to reach, including both idle and in-use
     * connection.
     *
     * <p>
     *
     * @param size The maximum number of connections
     * @return This Object, for chaining
     * @see HikariConfig#setMaximumPoolSize(int)
     */
    public HikariClientBuilder setMaximumPoolSize(int size) {
        this.config.setMaximumPoolSize(size);
        return this;
    }

    /**
     * Set a MetricRegistry instance to use for registration of metrics used by HikariCP.
     *
     * <p>
     *
     * @param registry The MetricRegistry instance for use
     * @return This Object, for chaining
     * @see HikariConfig#setMetricRegistry(Object)
     */
    public HikariClientBuilder setMetricRegistry(Object registry) {
        this.config.setMetricRegistry(registry);
        return this;
    }

    /**
     * Set the HealthRegistry that will be used for registration of health checks by HikariCP.
     *
     * <p>
     *
     * @param registry The HikariRegistry to be used
     * @return This Object, for chaining
     * @see HikariConfig#setHealthCheckRegistry(Object)
     */
    public HikariClientBuilder setHealthCheckRegistry(Object registry) {
        this.config.setHealthCheckRegistry(registry);
        return this;
    }

    /**
     * Set the name of the connection pool.
     *
     * <p>
     *
     * @param name The name of connection pool to use
     * @return This Object, for chaining
     * @see HikariConfig#setPoolName(String)
     */
    public HikariClientBuilder setPoolName(String name) {
        this.config.setPoolName(name);
        return this;
    }

    /**
     * Build the HikariCP client.
     *
     * <p>
     *
     * @return {@link HikariCP}
     */
    public HikariCP build() {
        return new HikariCP(config, new HikariDataSource(config), reconnect);
    }

    /**
     * Gets the {@link HikariConfig}.
     *
     * <p>
     *
     * @return {@link HikariConfig}
     */
    public HikariConfig getConfig() {
        return config;
    }

    /**
     * Sets the {@link HikariConfig}.
     *
     * <p>
     *
     * @param config The {@link HikariConfig} to set
     * @return This Object, for chaining
     */
    public HikariClientBuilder setConfig(HikariConfig config) {
        this.config = config;
        return this;
    }

    /**
     * Checks if its auto re-connectable.
     *
     * <p>
     *
     * @return <strong>{@code true}</strong> if auto reconnect is enabled, else false
     */
    public boolean isReconnect() {
        return reconnect;
    }
}
