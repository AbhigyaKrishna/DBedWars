package org.zibble.dbedwars.io;

import org.zibble.dbedwars.configuration.PluginFiles;
import org.zibble.dbedwars.utils.PluginFileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class ExternalLibrary {

    private static final Map<String, ExternalLibrary> LIBRARIES = new HashMap<>();

    public static final ExternalLibrary H2_DATABASE = ExternalLibrary.declare("https://search.maven.org/remotecontent?filepath=com/h2database/h2/2.1.210/h2-2.1.210.jar",
            "h2-database.jar", "H2-Database", "org.h2.Driver");
    public static final ExternalLibrary POSTGRESQL_DATABASE = ExternalLibrary.declare("https://search.maven.org/remotecontent?filepath=org/postgresql/postgresql/42.2.5/postgresql-42.2.5.jar",
            "postgresql-database.jar", "PostGreSQL-Database", "org.postgresql.Driver");
    public static final ExternalLibrary SQLITE_DATABASE = ExternalLibrary.declare("https://search.maven.org/remotecontent?filepath=org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar",
            "sqlite-database.jar", "SQLite-Database", "org.sqlite.JDBC");
    public static final ExternalLibrary MONGO_DATABASE = ExternalLibrary.declare("https://search.maven.org/remotecontent?filepath=org/mongodb/mongo-java-driver/3.9.0/mongo-java-driver-3.9.0.jar",
            "mongo-database.jar", "Mongo-Database", "com.mongodb.MongoClient");
    public static final ExternalLibrary HIKARI_CP = ExternalLibrary.declare("https://search.maven.org/remotecontent?filepath=com/zaxxer/HikariCP/3.2.0/HikariCP-3.2.0.jar",
            "hikari-cp.jar", "Hikari-CP", "com.zaxxer.hikari.HikariDataSource");
    public static final ExternalLibrary REACTIVE_STREAMS = ExternalLibrary.declare("https://repo1.maven.org/maven2/org/reactivestreams/reactive-streams/1.0.3/reactive-streams-1.0.3.jar",
            "reactive-streams.jar", "Reactive-Streams", "org.reactivestreams.Publisher");
    public static final ExternalLibrary JOOQ = ExternalLibrary.declare("https://repo1.maven.org/maven2/org/jooq/jooq/3.14.15/jooq-3.14.15.jar",
            "jooq.jar", "Jooq", "org.jooq.DSLContext", REACTIVE_STREAMS);

    private final String url;
    private final File file;
    private final String name;

    public ExternalLibrary(String url, String fileName, String name) {
        this.url = url;
        this.file = new File(PluginFiles.Folder.LIBRARIES_CACHE, fileName);
        this.name = name;
    }

    public static ExternalLibrary declare(String url, String fileName, String name, String testClass, ExternalLibrary... transitiveDependencies) {
        ExternalLibrary lib = new ExternalLibrary(url, fileName, name) {
            @Override
            public boolean isLoaded() {
                try {
                    Class.forName(testClass);
                    return true;
                } catch (ClassNotFoundException | IllegalAccessError e) {
                    return false;
                }
            }

            @Override
            public ExternalLibrary[] getTransitiveDependencies() {
                return transitiveDependencies;
            }
        };
        LIBRARIES.put(name, lib);
        return lib;
    }

    public static ExternalLibrary get(String name) {
        return LIBRARIES.get(name);
    }

    public static boolean downloadAll() {
        boolean success = true;
        for (ExternalLibrary lib : LIBRARIES.values()) {
            success = success && lib.download();
        }
        return success;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean download() {
        return PluginFileUtil.downloadFile(url, file);
    }

    public void load() {
        if (!exists()) {
            throw new IllegalStateException("Library " + name + " does not exist!");
        }
        PluginFileUtil.loadJar(file);
    }

    public ExternalLibrary[] getTransitiveDependencies() {
        return new ExternalLibrary[0];
    }

    public void loadSafely() {
        if (this.isLoaded()) return;

        if (!this.exists()) {
            this.download();
        }

        this.load();

        for (ExternalLibrary lib : this.getTransitiveDependencies()) {
            lib.loadSafely();
        }
    }

    public abstract boolean isLoaded();

}
