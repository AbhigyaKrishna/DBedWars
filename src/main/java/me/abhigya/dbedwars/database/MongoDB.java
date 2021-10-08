package me.abhigya.dbedwars.database;

import com.mongodb.DBCollection;
import me.Abhigya.core.database.mongo.MongoDocument;
import me.abhigya.dbedwars.DBedwars;

public class MongoDB extends DatabaseBridge{

    private final me.Abhigya.core.database.mongo.MongoDB mongoDatabase;
    private MongoDocument document;

    public MongoDB(DBedwars plugin, String host, int port) {
        super(plugin);
        mongoDatabase = new me.Abhigya.core.database.mongo.MongoDB(plugin,host, port);
    }

    @Override
    public void init() {
        mongoDatabase.connect();
        DBCollection collection = mongoDatabase.getCollection("PLAYER_STATS");
        document = new MongoDocument(collection);
    }

    @Override
    public void disconnect() {
        mongoDatabase.disconnect();
    }
}
