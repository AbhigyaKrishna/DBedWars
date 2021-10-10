package me.abhigya.dbedwars.database;

import com.google.gson.Gson;
import me.Abhigya.core.database.sql.SQLConsumer;
import me.Abhigya.core.database.sql.SQLDatabase;
import me.Abhigya.core.util.json.Json;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.cache.DataCache;
import me.abhigya.dbedwars.cache.PlayerStats;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class DatabaseBridge {

    protected static final Map< String, Map.Entry< List< String >, Class< ? extends DataCache > > > DATABASE_NAME_COLUMNS = new LinkedHashMap<>( );

    static {
        DATABASE_NAME_COLUMNS.put( "PLAYER_STATS", new AbstractMap.SimpleEntry<>( Arrays.asList( "UUID", "NAME", "LEVEL",
                "LEVEL_PROGRESS", "COINS", "WINSTREAK", "POINTS" ), PlayerStats.class ) );
    }

    private final DBedwars plugin;
    private final Gson gson;

    public DatabaseBridge( DBedwars plugin ) {
        this.plugin = plugin;
        this.gson = new Gson();
    }

    public abstract void init( );

    public abstract void disconnect( );

    public abstract Json getData( String key, Object gate, String database );

    public abstract List< DataCache > getAsDataCache( String key, Object gate, String database );

    public DBedwars getPlugin( ) {
        return plugin;
    }

    public Gson getGson( ) {
        return gson;
    }

    protected void querySQLFile( SQLDatabase db, String fileName ) throws IOException {
        InputStream sql = this.getPlugin( ).getResource( fileName );
        String s = IOUtils.toString( sql, StandardCharsets.UTF_8 );
        db.queryAsync( s, new SQLConsumer< ResultSet >( ) {
            @Override
            public void accept( ResultSet resultSet ) throws SQLException {
            }
        } );
    }

}
