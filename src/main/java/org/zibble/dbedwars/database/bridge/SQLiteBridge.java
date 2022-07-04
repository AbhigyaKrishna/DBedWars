package org.zibble.dbedwars.database.bridge;

import org.zibble.dbedwars.configuration.PluginFiles;
import org.zibble.dbedwars.database.sql.sqlite.SQLite;

import java.io.File;

public class SQLiteBridge extends SQLDatabaseBridge {

    public SQLiteBridge() {
        super(new SQLite(new File(PluginFiles.Folder.PLUGIN_DATA_FOLDER, "database.db")));
    }

}
