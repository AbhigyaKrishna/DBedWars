package org.zibble.dbedwars.database;

import com.google.gson.Gson;
import com.pepedevs.radium.database.Database;
import com.pepedevs.radium.database.sql.SQLDatabase;
import com.pepedevs.radium.utils.json.Json;
import org.apache.commons.io.IOUtils;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.cache.DataCache;
import org.zibble.dbedwars.cache.PlayerStats;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class DatabaseBridge {

    protected static final Map<String, Map.Entry<List<String>, Class<? extends DataCache>>>
            DATABASE_NAME_COLUMNS = new LinkedHashMap<>();

    static {
        DATABASE_NAME_COLUMNS.put(
                "PLAYER_STATS",
                new AbstractMap.SimpleEntry<>(
                        Arrays.asList(
                                "UUID",
                                "NAME",
                                "LEVEL",
                                "LEVEL_PROGRESS",
                                "COINS",
                                "WINSTREAK",
                                "POINTS"),
                        PlayerStats.class));
    }

    private final DBedwars plugin;
    private final Gson gson;

    public DatabaseBridge(DBedwars plugin) {
        this.plugin = plugin;
        this.gson = new Gson();
    }

    public abstract void init();

    public abstract void disconnect();

    public abstract Json getData(String key, Object gate, String database);

    public abstract List<DataCache> getAsDataCache(String key, Object gate, String database);

    public abstract boolean saveData(DataCache data, String database, boolean update);

    public abstract Database getHandle();

    public DBedwars getPlugin() {
        return this.plugin;
    }

    public Gson getGson() {
        return this.gson;
    }

    protected void querySQLFile(SQLDatabase db, String fileName) throws IOException {
        InputStream sql = this.getPlugin().getResource(fileName);
        String s = IOUtils.toString(sql, StandardCharsets.UTF_8);
        sql.close();
        CompletableFuture.runAsync(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PreparedStatement ps = db.getConnection().prepareStatement(s);
                            ps.execute();
                            ps.close();
                        } catch (SQLException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
