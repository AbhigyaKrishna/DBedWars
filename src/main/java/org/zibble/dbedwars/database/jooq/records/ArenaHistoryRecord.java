package org.zibble.dbedwars.database.jooq.records;

import org.jooq.impl.TableRecordImpl;
import org.zibble.dbedwars.database.data.ArenaHistory;
import org.zibble.dbedwars.database.jooq.tables.ArenaHistoryTable;

public class ArenaHistoryRecord extends TableRecordImpl<ArenaHistoryRecord> {

    public static ArenaHistoryRecord from(ArenaHistory data) {
        ArenaHistoryTable table = ArenaHistoryTable.ARENA_HISTORY;
        ArenaHistoryRecord record = new ArenaHistoryRecord();
        record.set(table.ID, data.getId());
        record.set(table.GAME_ID, data.getGameId());
        record.set(table.TEAM, data.getTeams());
        record.set(table.WINNER, data.getWinner());
        record.set(table.RUNTIME, data.getRuntime());
        record.set(table.TIMESTAMP, data.getTimestamp());
        record.set(table.ITEM_PICKUP, data.getItemPickup());
        record.set(table.DEATHS, data.getDeaths());
        record.set(table.BEDS_BROKEN, data.getBedsBroken());
        return record;
    }

    public ArenaHistoryRecord() {
        super(ArenaHistoryTable.ARENA_HISTORY);
    }

    public ArenaHistory toDataCache() {
        ArenaHistoryTable table = ArenaHistoryTable.ARENA_HISTORY;
        ArenaHistory data = new ArenaHistory();
        data.setId(this.get(table.ID));
        data.setGameId(this.get(table.GAME_ID));
        data.setTeams(this.get(table.TEAM));
        data.setWinner(this.get(table.WINNER));
        data.setRuntime(this.get(table.RUNTIME));
        data.setTimestamp(this.get(table.TIMESTAMP));
        data.setItemPickup(this.get(table.ITEM_PICKUP));
        data.setDeaths(this.get(table.DEATHS));
        data.setBedsBroken(this.get(table.BEDS_BROKEN));
        return data;
    }

}
