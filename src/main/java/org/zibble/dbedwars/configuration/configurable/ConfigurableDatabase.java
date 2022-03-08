package org.zibble.dbedwars.configuration.configurable;

import com.pepedevs.radium.database.DatabaseType;
import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;
import org.zibble.dbedwars.utils.ConfigurationUtils;

public class ConfigurableDatabase implements Loadable {

    @ConfigPath
    private String database;

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
        return ConfigurationUtils.matchEnum(this.database, DatabaseType.values()) != null;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    public String getDatabase() {
        return database;
    }

    public ConfigurableMySQL getMysql() {
        return mysql;
    }

    public ConfigurableMongoDB getMongodb() {
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

        @Override
        public String toString() {
            return "ConfigurableMySQL{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", databaseName='" + databaseName + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", reconnect=" + reconnect +
                    ", ssl=" + ssl +
                    '}';
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

        @Override
        public String toString() {
            return "ConfigurableMongoDB{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", databaseName='" + databaseName + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }

    }

    @Override
    public String toString() {
        return "ConfigurableDatabase{" +
                "database='" + database + '\'' +
                ", mySQL=" + mysql +
                ", mongoDB=" + mongodb +
                '}';
    }
}
