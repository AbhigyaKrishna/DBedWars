package org.zibble.dbedwars.database.bridge;

import org.zibble.dbedwars.configuration.configurable.ConfigurableDatabase;
import org.zibble.dbedwars.database.sql.postgresql.PostGreSQL;

public class PostGreSqlBridge extends SQLDatabaseBridge {

    public PostGreSqlBridge(ConfigurableDatabase.ConfigurableMySQL cfg) {
        super(new PostGreSQL(cfg.getHost(), cfg.getPort(), cfg.getDatabaseName(), cfg.getUsername(), cfg.getPassword(), cfg.getParams(), createHikariConfig()));
    }

}
