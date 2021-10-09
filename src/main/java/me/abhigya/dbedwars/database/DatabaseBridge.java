package me.abhigya.dbedwars.database;

import me.Abhigya.core.database.sql.SQLConsumer;
import me.Abhigya.core.database.sql.SQLDatabase;
import me.Abhigya.core.util.json.Json;
import me.abhigya.dbedwars.DBedwars;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class DatabaseBridge {

    protected static final Map< String, List< String > > DATABASE_NAME_COLUMNS = new LinkedHashMap<>( );

    static {
        DATABASE_NAME_COLUMNS.put( "PLAYER_STATS", Arrays.asList( "UUID", "NAME", "LEVEL", "LEVEL_PROGRESS", "COINS", "WINSTREAK", "POINTS" ) );
    }

    private final DBedwars plugin;

    public DatabaseBridge( DBedwars plugin ) {
        this.plugin = plugin;
    }

    public abstract void init( );

    public abstract void disconnect( );

    public abstract Json getData( String key, Object gate, String database );

    public DBedwars getPlugin( ) {
        return plugin;
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
