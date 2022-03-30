package org.zibble.dbedwars.database.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import org.zibble.dbedwars.api.util.Builder;

import java.util.Properties;

/** Class for creating Hikari client. */
public class HikariConfigBuilder implements Builder<HikariConfig> {

    private final HikariConfig config;

    public HikariConfigBuilder() {
        this.config = new HikariConfig();
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
    public HikariConfigBuilder addProperty(String key, String value) {
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
    public HikariConfigBuilder setDataSourceClassName(String className) {
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
    public HikariConfigBuilder setProperty(Properties property) {
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
    public HikariConfigBuilder setAutoCommit(boolean value) {
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
    public HikariConfigBuilder setConnectionTimeout(long timeout) {
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
    public HikariConfigBuilder setIdleTimeout(long timeout) {
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
    public HikariConfigBuilder setKeepAliveTime(long time) {
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
    public HikariConfigBuilder setMaxLifeTime(long time) {
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
    public HikariConfigBuilder setConnectionTestQuery(String query) {
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
    public HikariConfigBuilder setMinimumIdle(int minIdle) {
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
    public HikariConfigBuilder setMaximumPoolSize(int size) {
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
    public HikariConfigBuilder setMetricRegistry(Object registry) {
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
    public HikariConfigBuilder setHealthCheckRegistry(Object registry) {
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
    public HikariConfigBuilder setPoolName(String name) {
        this.config.setPoolName(name);
        return this;
    }

    public HikariConfig build() {
        return this.config;
    }

}
