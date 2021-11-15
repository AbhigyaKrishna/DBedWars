package com.pepedevs.dbedwars.database;

import me.Abhigya.core.database.Database;
import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.json.Json;
import me.Abhigya.core.util.reflection.general.FieldReflection;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.cache.DataCache;
import com.pepedevs.dbedwars.utils.JSONBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLite extends DatabaseBridge {

    private me.Abhigya.core.database.sql.sqlite.SQLite db;

    public SQLite(DBedwars plugin) {
        super(plugin);
        this.db =
                new me.Abhigya.core.database.sql.sqlite.SQLite(
                        new File(this.getPlugin().getDataFolder(), "database.db"), true);
    }

    @Override
    public void init() {
        try {
            this.db.connect();
            this.querySQLFile(this.db, "sql/stats_database_init.sql");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            this.db.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Json getData(String key, Object gate, String database) {
        try {
            for (Map.Entry<String, Map.Entry<List<String>, Class<? extends DataCache>>> entry :
                    DATABASE_NAME_COLUMNS.entrySet()) {
                if (database.equals(entry.getKey())) {
                    PreparedStatement ps =
                            this.db
                                    .getConnection()
                                    .prepareStatement(
                                            "SELECT * FROM " + database + " WHERE " + key + " = ?");
                    ps.setObject(1, gate);
                    ResultSet rs = this.db.query(ps);
                    int i = 0;
                    JSONBuilder jsonBuilder = new JSONBuilder();
                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        JSONBuilder inner = new JSONBuilder();
                        for (String col : entry.getValue().getKey()) {
                            Object object = rs.getObject(col);
                            inner.add(col, object);
                        }

                        jsonBuilder.add(String.valueOf(i), inner.getRawJson().getHandle());
                        i++;
                    }

                    rs.close();
                    ps.close();

                    return found ? jsonBuilder.getRawJson() : null;
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<DataCache> getAsDataCache(String key, Object gate, String database) {
        List<DataCache> caches = new ArrayList<>();
        try {
            for (Map.Entry<String, Map.Entry<List<String>, Class<? extends DataCache>>> entry :
                    DATABASE_NAME_COLUMNS.entrySet()) {
                if (database.equals(entry.getKey())) {
                    PreparedStatement ps =
                            this.db
                                    .getConnection()
                                    .prepareStatement(
                                            "SELECT * FROM " + database + " WHERE " + key + " = ?");
                    ps.setObject(1, gate);
                    ResultSet rs = this.db.query(ps);
                    while (rs.next()) {
                        JSONBuilder builder = new JSONBuilder();
                        for (String col : entry.getValue().getKey()) {
                            Object object = rs.getObject(col);
                            builder.add(col, object);
                        }

                        caches.add(
                                this.getGson()
                                        .fromJson(
                                                builder.getRawJson().toString(),
                                                entry.getValue().getValue()));
                    }

                    rs.close();
                    ps.close();
                    break;
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return caches;
    }

    @Override
    public boolean saveData(DataCache data, String database, boolean update) {
        Json json = Json.loadFromString(this.getGson().toJson(data));

        Map.Entry<List<String>, Class<? extends DataCache>> entry =
                DATABASE_NAME_COLUMNS.getOrDefault(database, null);
        if (entry == null) return false;

        if (update) {
            StringBuilder builder = new StringBuilder("UPDATE `" + database + "` SET ");
            for (String col : entry.getKey()) {
                if (col.equals("UUID")) continue;

                builder.append("`").append(col).append("` = ?, ");
            }
            if (builder.toString().trim().endsWith(",")) {
                builder = new StringBuilder(StringUtils.chop(builder.toString().trim()));
                builder.append(" WHERE `UUID` = '")
                        .append(json.getAsJsonPrimitive("UUID").getAsString())
                        .append("';");
            }

            try {
                PreparedStatement statement =
                        this.db.getConnection().prepareStatement(builder.toString());
                byte b = 1;
                for (String col : entry.getKey()) {
                    if (col.equals("UUID")) continue;

                    statement.setObject(
                            b, FieldReflection.getValue(json.getAsJsonPrimitive(col), "value"));
                    b++;
                }

                this.db.update(statement);
            } catch (SQLException | IOException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder builder = new StringBuilder("INSERT INTO `" + database + "` (");
            StringBuilder column = new StringBuilder();
            StringBuilder val = new StringBuilder();
            for (String col : entry.getKey()) {
                if (column.length() != 0) column.append(", ");
                column.append("`").append(col).append("`");

                if (val.length() != 0) val.append(", ");
                val.append("?");
            }

            builder.append(column).append(") VALUES (").append(val).append(");");

            try {
                PreparedStatement statement =
                        this.db.getConnection().prepareStatement(builder.toString());
                byte b = 1;
                for (String col : entry.getKey()) {
                    statement.setObject(
                            b, FieldReflection.getValue(json.getAsJsonPrimitive(col), "value"));
                    b++;
                }

                this.db.update(statement);
            } catch (SQLException | IOException | IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public Database getHandle() {
        return this.db;
    }
}
