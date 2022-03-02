package org.zibble.dbedwars.database.bridge;

import org.zibble.dbedwars.configuration.configurable.ConfigurableDatabase;
import org.zibble.dbedwars.database.sql.mysql.MySQL;

public class MySQLBridge extends SQLDatabaseBridge {

    public MySQLBridge(ConfigurableDatabase.ConfigurableMySQL cfg) {
        super(new MySQL(cfg.getHost(), cfg.getPort(), cfg.getDatabaseName(), cfg.getUsername(), cfg.getPassword(), cfg.isReconnect(), cfg.isSsl()));
    }

}
