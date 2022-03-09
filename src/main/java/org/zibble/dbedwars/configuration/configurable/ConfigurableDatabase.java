package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.database.DatabaseType;

public class ConfigurableDatabase implements Loadable {

    @ConfigPath
    private DatabaseType database;

    @ConfigPath
    private ConfigurableMySQL mysql;

    @ConfigPath
    private ConfigurableMongoDB mongodb;

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return this.database != null;
    }

    public DatabaseType getDatabase() {
        return database;
    }

    public ConfigurableMySQL getMySQL() {
        return mysql;
    }

    public ConfigurableMongoDB getMongoDB() {
        return mongodb;
    }

    public static class ConfigurableMySQL implements Loadable {

        @ConfigPath
        private String host;

        @ConfigPath
        private int port;

        @ConfigPath
        private String databaseName;

        @ConfigPath
        private String username;

        @ConfigPath
        private String password;

        @ConfigPath
        private boolean reconnect;

        @ConfigPath
        private boolean ssl;

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
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

        @ConfigPath
        private String host;

        @ConfigPath
        private int port;

        @ConfigPath
        private String databaseName;

        @ConfigPath
        private String username;

        @ConfigPath
        private String password;

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
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

    }

}
