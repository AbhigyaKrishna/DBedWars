package org.zibble.dbedwars.database.data.table;

import org.zibble.dbedwars.database.data.DataCache;
import org.zibble.dbedwars.database.data.PlayerStats;
import org.zibble.dbedwars.database.data.QuickBuy;

public class DataTables {

    public static final DataTable<PlayerStats> STATS = define("player_stats", PlayerStats.class, new PlayerStats());
    public static final DataTable<QuickBuy> QUICK_BUY = define("quick_buy", QuickBuy.class, new QuickBuy());

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
