package org.zibble.dbedwars.database.bridge;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.database.sql.h2.H2;

import java.io.File;

public class H2DatabaseBridge extends SQLDatabaseBridge {

    public H2DatabaseBridge() {
        super(new H2(new File(DBedwars.getInstance().getDataFolder(), "database.db"), true));
    }

}
