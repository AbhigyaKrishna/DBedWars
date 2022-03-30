package org.zibble.dbedwars.database.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.database.Database;
import org.zibble.dbedwars.database.DatabaseType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SQLDatabase extends Database {

    protected final HikariConfig config;
    protected HikariDataSource dataSource;

    protected Connection connection;

    public SQLDatabase(DatabaseType type, HikariConfig config) {
        super(type);
        this.config = config;
    }

    @Override
    public boolean isConnected() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() throws IllegalStateException, SQLException {
        return this.connection = this.isConnected() ? this.connection : this.dataSource.getConnection();
    }

    @Override
    public void disconnect() throws SQLException {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected!");
        }

        this.connection.close();
        this.connection = null;
        this.dataSource.close();
    }

    public boolean execute(String query) throws SQLException {
        try (PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            return statement.execute();
        }
    }

    public boolean execute(PreparedStatement statement) throws SQLException {
        return statement.execute();
    }

    public ActionFuture<Boolean> executeAsync(String query) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return this.execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public ActionFuture<Boolean> executeAsync(PreparedStatement statement) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return this.execute(statement);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public ResultSet query(String query) throws SQLException {
        try (PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            return statement.executeQuery();
        }
    }

    public <R> R query(String query, SQLFunction<ResultSet, R> function) throws SQLException {
        try (PreparedStatement statement = this.getConnection().prepareStatement(query)) {
            return function.apply(statement.executeQuery());
        }
    }

    public ResultSet query(PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }

    public <R> R query(PreparedStatement statement, SQLFunction<ResultSet, R> function) throws SQLException {
        return function.apply(statement.executeQuery());
    }

    public void query(String query, SQLConsumer<ResultSet> consumer) throws SQLException {
        try (ResultSet resultSet = this.query(query)) {
            consumer.accept(resultSet);
        }
    }

    public void query(PreparedStatement statement, SQLConsumer<ResultSet> consumer) throws SQLException {
        try (ResultSet resultSet = this.query(statement)) {
            consumer.accept(resultSet);
        }
    }

    public ActionFuture<ResultSet> queryAsync(String query) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return this.query(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public <R> ActionFuture<R> queryAsync(String query, SQLFunction<ResultSet, R> function) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return this.query(query, function);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public ActionFuture<ResultSet> queryAsync(PreparedStatement statement) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return this.query(statement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public <R> ActionFuture<R> queryAsync(PreparedStatement statement, SQLFunction<ResultSet, R> function) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return this.query(statement, function);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public void queryAsync(String query, SQLConsumer<ResultSet> consumer) {
        ActionFuture.runAsync(() -> {
            try {
                this.query(query, consumer);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void queryAsync(PreparedStatement statement, SQLConsumer<ResultSet> consumer) {
        ActionFuture.runAsync(() -> {
            try {
                this.query(statement, consumer);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public int update(String update) throws SQLException {
        try (PreparedStatement statement = this.getConnection().prepareStatement(update)) {
            return statement.executeUpdate();
        }
    }

    public int update(PreparedStatement statement) throws SQLException {
        return statement.executeUpdate();
    }

    public ActionFuture<Integer> updateAsync(String update) {
        return ActionFuture.supplyAsync(() -> {
            int results = 0;
            try {
                results = this.update(update);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return results;
        });
    }

    public ActionFuture<Integer> updateAsync(PreparedStatement statement) {
        return ActionFuture.supplyAsync(() -> {
            int results = 0;
            try {
                results = this.update(statement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return results;
        });
    }

}
