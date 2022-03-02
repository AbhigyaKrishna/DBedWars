package org.zibble.dbedwars.database.sql;

import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.database.Database;
import org.zibble.dbedwars.database.DatabaseType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SQLDatabase extends Database {

    public SQLDatabase(DatabaseType type) {
        super(type);
    }

    public abstract Connection getConnection() throws IOException, IllegalStateException, SQLException;

    public abstract int getLostConnections();

    public boolean execute(String query) throws SQLException {
        try {
            PreparedStatement statement = this.getConnection().prepareStatement(query);
            boolean b = statement.execute();
            statement.close();
            return b;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean execute(PreparedStatement statement) throws SQLException {
        final boolean result = statement.execute();
        if (!statement.isClosed()) statement.close();
        return result;
    }

    public ActionFuture<Boolean> executeAsync(String query) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return execute(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public ActionFuture<Boolean> executeAsync(PreparedStatement statement) {
        return ActionFuture.supplyAsync(() -> {
            try {
                final boolean result = statement.execute();
                if (!statement.isClosed()) statement.close();
                return result;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public ResultSet query(String query) throws SQLException {
        try {
            PreparedStatement statement = this.getConnection().prepareStatement(query);
            return statement.executeQuery();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <R> R query(String query, SQLFunction<ResultSet, R> function) throws SQLException {
        try {
            PreparedStatement statement = this.getConnection().prepareStatement(query);
            return function.apply(statement.executeQuery());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet query(PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }

    public <R> R query(PreparedStatement statement, SQLFunction<ResultSet, R> function) throws SQLException {
        return function.apply(statement.executeQuery());
    }

    public void query(String query, SQLConsumer<ResultSet> consumer) throws SQLException {
        ResultSet resultSet = this.query(query);

        consumer.accept(resultSet);

        if (!resultSet.isClosed()) resultSet.close();
        if (!resultSet.getStatement().isClosed()) resultSet.getStatement().close();
    }

    public void query(PreparedStatement statement, SQLConsumer<ResultSet> consumer)
            throws SQLException {
        ResultSet resultSet = this.query(statement);

        consumer.accept(resultSet);

        if (!resultSet.isClosed()) resultSet.close();
        if (!resultSet.getStatement().isClosed()) resultSet.getStatement().close();
    }

    public ActionFuture<ResultSet> queryAsync(String query) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return SQLDatabase.this.query(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public <R> ActionFuture<R> queryAsync(String query, SQLFunction<ResultSet, R> function) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return SQLDatabase.this.query(query, function);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public ActionFuture<ResultSet> queryAsync(PreparedStatement statement) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return SQLDatabase.this.query(statement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public <R> ActionFuture<R> queryAsync(PreparedStatement statement, SQLFunction<ResultSet, R> function) {
        return ActionFuture.supplyAsync(() -> {
            try {
                return SQLDatabase.this.query(statement, function);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public void queryAsync(String query, SQLConsumer<ResultSet> consumer) {
        ActionFuture.runAsync(() -> {
            try {
                SQLDatabase.this.query(query, consumer);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void queryAsync(PreparedStatement statement, SQLConsumer<ResultSet> consumer) {
        ActionFuture.runAsync(() -> {
            try {
                SQLDatabase.this.query(statement, consumer);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public int update(String update) throws SQLException {
        try {
            PreparedStatement statement = this.getConnection().prepareStatement(update);
            int result = statement.executeUpdate();

            statement.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int update(PreparedStatement statement) throws SQLException {
        int result = statement.executeUpdate();

        statement.close();
        return result;
    }

    public ActionFuture<Integer> updateAsync(String update) {
        return ActionFuture.supplyAsync(() -> {
            int results = 0;
            try {
                results = SQLDatabase.this.update(update);
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
                results = SQLDatabase.this.update(statement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return results;
        });
    }

}
