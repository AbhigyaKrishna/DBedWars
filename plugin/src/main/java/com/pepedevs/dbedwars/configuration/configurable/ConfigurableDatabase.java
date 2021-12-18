package com.pepedevs.dbedwars.configuration.configurable;

import com.pepedevs.corelib.database.DatabaseType;
import com.pepedevs.corelib.utils.configuration.Loadable;
import com.pepedevs.corelib.utils.configuration.annotations.LoadableEntry;
import com.pepedevs.dbedwars.utils.ConfigurationUtils;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigurableDatabase implements Loadable {

    @LoadableEntry(key = "database")
    private String database;

    private ConfigurableMySQL mySQL;
    private ConfigurableMongoDB mongoDB;

    @Override
    public Loadable load(ConfigurationSection section) {
        this.mySQL = new ConfigurableMySQL();
        this.mySQL.load(section.getConfigurationSection("mysql"));
        this.mongoDB = new ConfigurableMongoDB();
        this.mongoDB.load(section.getConfigurationSection("mongodb"));
        return this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return ConfigurationUtils.matchEnum(this.database, DatabaseType.values()) != null;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    public String getDatabase() {
        return database;
    }

    public ConfigurableMySQL getMySQL() {
        return mySQL;
    }

    public ConfigurableMongoDB getMongoDB() {
        return mongoDB;
    }

    public static class ConfigurableMySQL implements Loadable {

        @LoadableEntry(key = "host")
        private String host;

        @LoadableEntry(key = "port")
        private int port;

        @LoadableEntry(key = "database-name")
        private String databaseName;

        @LoadableEntry(key = "username")
        private String username;

        @LoadableEntry(key = "password")
        private String password;

        @LoadableEntry(key = "reconnect")
        private boolean reconnect;

        @LoadableEntry(key = "ssl")
        private boolean ssl;

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public boolean isReconnect() {
            return reconnect;
        }

        public boolean isSsl() {
            return ssl;
        }
    }

    public static class ConfigurableMongoDB implements Loadable {

        @LoadableEntry(key = "host")
        private String host;

        @LoadableEntry(key = "port")
        private int port;

        @LoadableEntry(key = "database-name")
        private String databaseName;

        @Override
        public Loadable load(ConfigurationSection section) {
            return this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getDatabaseName() {
            return databaseName;
        }
    }
}
