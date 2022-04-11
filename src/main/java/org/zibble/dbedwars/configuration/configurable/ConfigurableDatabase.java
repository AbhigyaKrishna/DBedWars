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

    @ConfigPath("store-match-history")
    private MatchHistory matchHistory;

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

    public MatchHistory getMatchHistory() {
        return matchHistory;
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
        private String params;

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

        public String getParams() {
            return params == null ? "" : params;
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

    public static class MatchHistory implements Loadable {

        @ConfigPath
        private boolean enabled;

        @ConfigPath
        private String pruneTime;

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getPruneTime() {
            return pruneTime;
        }

        public void setPruneTime(String pruneTime) {
            this.pruneTime = pruneTime;
        }

    }

}
