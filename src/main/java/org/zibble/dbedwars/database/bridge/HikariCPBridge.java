package org.zibble.dbedwars.database.bridge;

import org.zibble.dbedwars.configuration.configurable.ConfigurableDatabase;
import org.zibble.dbedwars.database.sql.hikaricp.HikariClientBuilder;

public class HikariCPBridge extends SQLDatabaseBridge{

    public HikariCPBridge(ConfigurableDatabase.ConfigurableMySQL cfg) {
        super(new HikariClientBuilder(cfg.getHost(), cfg.getPort(), cfg.getDatabaseName(), cfg.getUsername(), cfg.getPassword(), cfg.isReconnect(), cfg.isSsl())
                .addProperty("cachePrepStmts", "true")
                .addProperty("prepStmtCacheSize", "250")
                .addProperty("prepStmtCacheSqlLimit", "2048")
                .addProperty("useServerPrepStmts", "true")
                .addProperty("useLocalSessionState", "true")
                .addProperty("rewriteBatchedStatements", "true")
                .addProperty("cacheResultSetMetadata", "true")
                .addProperty("cacheServerConfiguration", "true")
                .addProperty("elideSetAutoCommits", "true")
                .addProperty("maintainTimeStats", "false")
                .addProperty("keepaliveTime", "30000ms")
                .build());
    }

}
