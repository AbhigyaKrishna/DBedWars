package org.zibble.dbedwars.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang.Validate;
import org.bson.Document;
import org.zibble.dbedwars.database.Database;
import org.zibble.dbedwars.database.DatabaseType;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Class for interacting with a Mongo database. */
public class MongoDB extends Database {

    private final String ip;
    private final int port;
    private MongoClientOptions options;
    private final String dbname;
    private final String userName;
    private final String password;

    private MongoClient client;
    private MongoDatabase database;
    private final Set<MongoCollection<?>> collections = new HashSet<>();

    public MongoDB(String host, int port, String dbname) {
        this(host, port, dbname, MongoClientOptions.builder().build(), null, null);
    }

    public MongoDB(String host, int port, String dbname, MongoClientOptions options, String username, String password) {
        super(DatabaseType.MongoDB);
        Validate.isTrue(!host.isEmpty(), "Host Address cannot be null or empty!");
        this.ip = host;
        this.port = port;
        this.dbname = dbname;
        this.options = options;
        this.userName = username;
        this.password = password;
    }

    public void setOptions(MongoClientOptions options) {
        this.options = options;
    }

    @Override
    public boolean isConnected() {
        return this.client != null && this.database != null;
    }

    /** Starts the connection with MongoDB. */
    @Override
    public synchronized void connect() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        if (userName == null || password == null) {
            this.client = new MongoClient(new ServerAddress(this.ip, this.port), this.options);
        }else {
            this.client = new MongoClient(new ServerAddress(this.ip, this.port), MongoCredential.createCredential(this.userName, this.dbname, this.password.toCharArray()), this.options);
        }

        this.database = client.getDatabase(this.dbname);
    }

    /** Closes the connection with MongoDB. */
    @Override
    public void disconnect() {
        this.client.close();
        this.client = null;
        this.database = null;
    }

    public Set<MongoCollection<Document>> getCollections(String... name) {
        Set<MongoCollection<Document>> collections = new HashSet<>();
        for (String s : name) {
            collections.add(database.getCollection(s));
            this.collections.add(database.getCollection(s));
        }
        return collections;
    }

    public MongoCollection<Document> getCollection(String name) {
        MongoCollection<Document> collection = database.getCollection(name);
        this.collections.add(collection);
        return collection;
    }

    public <T> MongoCollection<T> getCollection(String name, Class<T> clazz) {
        MongoCollection<T> collection = database.getCollection(name, clazz);
        this.collections.add(collection);
        return collection;
    }

    public Set<MongoCollection<?>> getAllCachedCollection() {
        return this.collections;
    }

    public ListCollectionsIterable<Document> getAllRawCollectionsName() {
        return database.listCollections();
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoClient getClient() {
        return client;
    }
}
