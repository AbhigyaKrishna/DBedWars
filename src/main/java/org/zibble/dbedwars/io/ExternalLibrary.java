package org.zibble.dbedwars.io;

import org.zibble.dbedwars.configuration.PluginFiles;
import org.zibble.dbedwars.utils.PluginFileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class ExternalLibrary {

    private static final Map<String, ExternalLibrary> LIBRARIES = new HashMap<>();

    public static final ExternalLibrary H2_DATABASE = ExternalLibrary.declare("https://search.maven.org/remotecontent?filepath=com/h2database/h2/2.1.210/h2-2.1.210.jar",
            "h2-database.jar", "H2-Database", "org.h2.Driver");
    public static final ExternalLibrary POSTGRESQL_DATABASE = ExternalLibrary.declare("https://search.maven.org/remotecontent?filepath=org/postgresql/postgresql/42.2.5/postgresql-42.2.5.jar",
            "postgresql-database.jar", "PostGreSQL-Database", "org.postgresql.Driver");
    public static final ExternalLibrary SQLITE_DATABASE = ExternalLibrary.declare("https://search.maven.org/remotecontent?filepath=org/xerial/sqlite-jdbc/3.25.2/sqlite-jdbc-3.25.2.jar",
            "sqlite-database.jar", "SQLite-Database", "org.sqlite.JDBC");
    public static final ExternalLibrary MONGO_DATABASE = ExternalLibrary.declare("https://search.maven.org/remotecontent?filepath=org/mongodb/mongo-java-driver/3.9.0/mongo-java-driver-3.9.0.jar",
            "mongo-database.jar", "Mongo-Database", "com.mongodb.MongoClient");
    public static final ExternalLibrary HIKARI_CP = ExternalLibrary.declare("https://search.maven.org/remotecontent?filepath=com/zaxxer/HikariCP/3.2.0/HikariCP-3.2.0.jar",
            "hikari-cp.jar", "Hikari-CP", "com.zaxxer.hikari.HikariDataSource");

    public static ExternalLibrary declare(String url, String fileName, String name, String testClass) {
        ExternalLibrary lib = new ExternalLibrary(url, fileName, name) {
            @Override
            public boolean isLoaded() {
                try {
                    Class.forName(testClass);
                    return true;
                } catch (ClassNotFoundException e) {
                    return false;
                }
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

    private final String url;
    private final File file;
    private final String name;

    public ExternalLibrary(String url, String fileName, String name) {
        this.url = url;
        this.file = new File(PluginFiles.Folder.LIBRARIES_CACHE, fileName);
        this.name = name;
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
        return PluginFileUtils.downloadFile(url, file);
    }

    public void load() {
        if (!exists()) {
            throw new IllegalStateException("Library " + name + " does not exist!");
        }
        PluginFileUtils.loadJar(file);
    }

    public abstract boolean isLoaded();

}
