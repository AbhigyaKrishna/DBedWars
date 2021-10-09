package me.abhigya.dbedwars.database;

import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import me.Abhigya.core.database.mongo.MongoDocument;
import me.Abhigya.core.util.json.Json;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.utils.JSONBuilder;

import java.util.Set;

public class MongoDB extends DatabaseBridge {

    private final me.Abhigya.core.database.mongo.MongoDB db;
    private final Gson gson;

    public MongoDB( DBedwars plugin, String host, int port ) {
        super( plugin );
        this.db = new me.Abhigya.core.database.mongo.MongoDB( plugin, host, port );
        this.gson = new Gson( );
    }

    @Override
    public void init( ) {
        this.db.connect( );
    }

    @Override
    public void disconnect( ) {
        this.db.disconnect( );
    }

    @Override
    public Json getData( String key, Object gate, String database ) {
        DBCollection collection = this.db.getCollection( database );
        MongoDocument document = new MongoDocument( collection );
        Set< DBObject > objects = document.getDocument( key, gate );
        JSONBuilder builder = new JSONBuilder();
        int i = 0;
        boolean found = false;
        for ( DBObject object : objects ) {
            found = true;
            Json json = Json.loadFromString( this.gson.toJson( object ) );
            builder.add( String.valueOf( i ), json.getHandle( ) );
            i++;
        }

        return found ? builder.getRawJson( ) : null;
    }

}
