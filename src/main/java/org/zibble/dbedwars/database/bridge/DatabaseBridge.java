package org.zibble.dbedwars.database.bridge;

import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.Initializable;
import org.zibble.dbedwars.database.data.ArenaHistory;
import org.zibble.dbedwars.database.data.PlayerDataCache;
import org.zibble.dbedwars.database.data.table.DataTable;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

public interface DatabaseBridge extends Initializable {

    void init();

    void disconnect();

    <T extends PlayerDataCache> ActionFuture<T> requestPlayerData(DataTable<T> dataTable, UUID uuid);

    <T extends PlayerDataCache> ActionFuture<Boolean> insertNewPlayerData(DataTable<T> dataTable, T dataCache);

    <T extends PlayerDataCache> ActionFuture<Boolean> updatePlayerData(DataTable<T> dataTable, T dataCache);

    ActionFuture<Boolean> insertArenaHistory(ArenaHistory history);

    Collection<ArenaHistory> getArenaHistory(String arenaId);

}
