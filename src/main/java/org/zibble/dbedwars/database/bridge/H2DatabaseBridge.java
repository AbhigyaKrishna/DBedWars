package org.zibble.dbedwars.database.bridge;

import org.zibble.dbedwars.configuration.PluginFiles;
import org.zibble.dbedwars.database.sql.h2.H2;

import java.io.File;

public class H2DatabaseBridge extends SQLDatabaseBridge {

    public H2DatabaseBridge() {
        super(new H2(new File(PluginFiles.Folder.PLUGIN_DATA_FOLDER, "database.db"), createHikariConfig()));
    }

}
