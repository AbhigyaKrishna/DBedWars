package org.zibble.dbedwars.database.data.table;

import org.zibble.dbedwars.database.data.DataCache;

public interface DataTable<T extends DataCache> {

    String database();

    Class<T> clazz();

    T newInstance();

}
