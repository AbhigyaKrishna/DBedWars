package org.zibble.dbedwars.database.bridge;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.database.sql.mysql.MySQL;

public class MySQLBridge extends SQLDatabaseBridge {

    public MySQLBridge(String host, int port, String database, @NotNull String username, @NotNull String password, boolean reconnect, boolean ssl) {
        super(new MySQL(host, port, database, username, password, reconnect, ssl));
    }

}
