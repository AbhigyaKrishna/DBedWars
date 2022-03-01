package org.zibble.dbedwars.database.data.table;

import org.zibble.dbedwars.database.data.DataCache;
import org.zibble.dbedwars.database.data.PlayerStats;

public class DataTables {

    public static DataTable<PlayerStats> STATS = define("player_stats", PlayerStats.class, new PlayerStats());

    public static <T extends DataCache> DataTable<T> define(String database, Class<T> clazz, T object) {
        return new DataTable<T>() {
            @Override
            public String database() {
                return database;
            }

            @Override
            public Class<T> clazz() {
                return clazz;
            }

            @Override
            public T newInstance() {
                return (T) object.copy();
            }
        };
    }

}
