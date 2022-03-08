package org.zibble.dbedwars.database.bridge;

import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.util.Initializable;
import org.zibble.dbedwars.database.data.PlayerDataCache;
import org.zibble.dbedwars.database.data.table.DataTable;

import java.util.UUID;

public interface DatabaseBridge extends Initializable {

    void init();

    void disconnect();

    <T extends PlayerDataCache> ActionFuture<T> requestPlayerData(DataTable<T> dataTable, UUID uuid);

    <T extends PlayerDataCache> ActionFuture<Boolean> insertNewPlayerData(DataTable<T> dataTable, T dataCache);

    <T extends PlayerDataCache> ActionFuture<Boolean> updatePlayerData(DataTable<T> dataTable, T dataCache);

}