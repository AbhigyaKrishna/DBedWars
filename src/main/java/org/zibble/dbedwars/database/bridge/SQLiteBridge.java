package org.zibble.dbedwars.database.bridge;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.database.sql.sqlite.SQLite;

import java.io.File;

public class SQLiteBridge extends SQLDatabaseBridge {

    public SQLiteBridge() {
        super(new SQLite(new File(DBedwars.getInstance().getDataFolder(), "database.db"), true));
    }

}
