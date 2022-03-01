package org.zibble.dbedwars.database.data;

import java.util.UUID;

public interface PlayerDataCache extends DataCache {

    UUID getUuid();

    String getName();

}
