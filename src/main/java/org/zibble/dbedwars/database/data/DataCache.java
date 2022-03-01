package org.zibble.dbedwars.database.data;

import org.zibble.dbedwars.api.util.properies.PropertySerializable;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

public interface DataCache extends PropertySerializable {

    void load(DataReader<?> reader) throws Exception;

    void save(DataWriter<?> writer) throws Exception;

    DataCache copy();

}
