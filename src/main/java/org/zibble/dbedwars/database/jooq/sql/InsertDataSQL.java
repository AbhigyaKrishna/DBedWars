package org.zibble.dbedwars.database.jooq.sql;

import org.jooq.DSLContext;
import org.zibble.dbedwars.database.data.ArenaHistory;
import org.zibble.dbedwars.database.data.PlayerStats;
import org.zibble.dbedwars.database.data.QuickBuy;
import org.zibble.dbedwars.database.jooq.tables.ArenaHistoryTable;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;
import org.zibble.dbedwars.database.jooq.tables.QuickBuyTable;

public class InsertDataSQL implements ISQL {

    private final DSLContext dsl;

    public InsertDataSQL(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public DSLContext context() {
        return this.dsl;
    }

    public Stage<Boolean> insertPlayerStat(PlayerStats stat) {
        return Stage.of(() -> this.dsl.insertInto(PlayerStatTable.PLAYER_STAT)
                .set(PlayerStatTable.PLAYER_STAT.UUID, stat.getUuid())
                .set(PlayerStatTable.PLAYER_STAT.NAME, stat.getName())
                .set(PlayerStatTable.PLAYER_STAT.LEVEL, stat.getLevel().get())
                .set(PlayerStatTable.PLAYER_STAT.LEVEL_PROGRESS, stat.getLevelProgress().get())
                .set(PlayerStatTable.PLAYER_STAT.COINS, stat.getCoins().get())
                .set(PlayerStatTable.PLAYER_STAT.WINSTREAK, stat.getWinStreak().shortValue())
                .set(PlayerStatTable.PLAYER_STAT.POINTS, stat.getPoints().get())
                .set(PlayerStatTable.PLAYER_STAT.KILLS, stat.getKills())
                .set(PlayerStatTable.PLAYER_STAT.FINAL_KILLS, stat.getFinalKills())
                .set(PlayerStatTable.PLAYER_STAT.DEATHS, stat.getDeaths())
                .set(PlayerStatTable.PLAYER_STAT.BED_BROKEN, stat.getBedBroken())
                .set(PlayerStatTable.PLAYER_STAT.BED_LOST, stat.getBedLost())
                .set(PlayerStatTable.PLAYER_STAT.WINS, stat.getWins())
                .set(PlayerStatTable.PLAYER_STAT.PLAYED, stat.getPlayed())
                .onDuplicateKeyIgnore()
                .execute() > 0);
    }

    public Stage<Boolean> insertQuickBuy(QuickBuy quickBuy) {
        return Stage.of(() -> this.dsl.insertInto(QuickBuyTable.QUICK_BUY)
                .set(QuickBuyTable.QUICK_BUY.UUID, quickBuy.getUuid())
                .set(QuickBuyTable.QUICK_BUY.NAME, quickBuy.getName())
                .set(QuickBuyTable.QUICK_BUY.QUICK_BUY_DATA, quickBuy.getData())
                .onDuplicateKeyIgnore()
                .execute() > 0);
    }

    public Stage<Boolean> insertArenaHistory(ArenaHistory history) {
        return Stage.of(() -> this.dsl.insertInto(ArenaHistoryTable.ARENA_HISTORY)
                .set(ArenaHistoryTable.ARENA_HISTORY.ID, history.getId())
                .set(ArenaHistoryTable.ARENA_HISTORY.GAME_ID, history.getGameId())
                .set(ArenaHistoryTable.ARENA_HISTORY.TEAM, history.getTeams())
                .set(ArenaHistoryTable.ARENA_HISTORY.WINNER, history.getWinner())
                .set(ArenaHistoryTable.ARENA_HISTORY.RUNTIME, history.getRuntime())
                .set(ArenaHistoryTable.ARENA_HISTORY.TIMESTAMP, history.getTimestamp())
                .set(ArenaHistoryTable.ARENA_HISTORY.ITEM_PICKUP, history.getItemPickup())
                .set(ArenaHistoryTable.ARENA_HISTORY.DEATHS, history.getDeaths())
                .set(ArenaHistoryTable.ARENA_HISTORY.BEDS_BROKEN, history.getBedsBroken())
                .execute() > 0);
    }

}
