package me.abhigya.dbedwars.database;

import me.Abhigya.core.util.json.Json;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.utils.JSONBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SQLite extends DatabaseBridge {

    private me.Abhigya.core.database.sql.sqlite.SQLite db;

    public SQLite( DBedwars plugin ) {
        super( plugin );
        this.db = new me.Abhigya.core.database.sql.sqlite.SQLite( this.getPlugin( ), new File( this.getPlugin( ).getDataFolder( ), "database.db" ), true );
    }

    @Override
    public void init( ) {
        try {
            this.db.connect( );
            this.querySQLFile( this.db, "sql.database_init.sql" );
        } catch ( IOException | SQLException e ) {
            e.printStackTrace( );
        }
    }

    @Override
    public void disconnect( ) {
        try {
            this.db.disconnect( );
        } catch ( SQLException e ) {
            e.printStackTrace( );
        }
    }

    @Override
    public Json getData( String key, Object gate, String database ) {
        try {
            for ( Map.Entry< String, List< String > > entry : DATABASE_NAME_COLUMNS.entrySet( ) ) {
                if ( database.equals( entry.getKey( ) ) ) {
                    PreparedStatement ps = this.db.getConnection( ).prepareStatement( "SELECT * FROM " + database + " WHERE " + key + " = ?" );
                    ps.setObject( 1, gate );
                    ResultSet rs = this.db.query( ps );
                    int i = 0;
                    JSONBuilder jsonBuilder = new JSONBuilder( );
                    boolean found = false;
                    while ( rs.next( ) ) {
                        found = true;
                        JSONBuilder inner = new JSONBuilder( );
                        for ( String col : entry.getValue( ) ) {
                            Object object = rs.getObject( col );
                            inner.add( col, object );
                        }

                        jsonBuilder.add( String.valueOf( i ), inner.getRawJson( ).getHandle( ) );
                        i++;
                    }

                    rs.close( );
                    ps.close( );

                    return found ? jsonBuilder.getRawJson( ) : null;
                }
            }
        } catch ( SQLException | IOException e ) {
            e.printStackTrace( );
        }

        return null;
    }

}
