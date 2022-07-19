package org.zibble.dbedwars.database.jooq.sql;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.zibble.dbedwars.database.data.table.DataTables;
import org.zibble.dbedwars.database.jooq.tables.ArenaHistoryTable;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;
import org.zibble.dbedwars.database.jooq.tables.QuickBuyTable;

public class CreateTableSQL implements ISQL {

    private final DSLContext context;

    public CreateTableSQL(DSLContext context) {
        this.context = context;
    }

    @Override
    public DSLContext context() {
        return this.context;
    }

    public ISQL.Stage<Integer> createStatsTable() {
        return Stage.of(() -> this.context.createTableIfNotExists(DSL.name(DataTables.STATS.database()))
                .column(PlayerStatTable.PLAYER_STAT.UUID)
                .column(PlayerStatTable.PLAYER_STAT.NAME)
                .column(PlayerStatTable.PLAYER_STAT.LEVEL)
                .column(PlayerStatTable.PLAYER_STAT.LEVEL_PROGRESS)
                .column(PlayerStatTable.PLAYER_STAT.COINS)
                .column(PlayerStatTable.PLAYER_STAT.WINSTREAK)
                .column(PlayerStatTable.PLAYER_STAT.POINTS)
                .column(PlayerStatTable.PLAYER_STAT.KILLS)
                .column(PlayerStatTable.PLAYER_STAT.FINAL_KILLS)
                .column(PlayerStatTable.PLAYER_STAT.DEATHS)
                .column(PlayerStatTable.PLAYER_STAT.BED_BROKEN)
                .column(PlayerStatTable.PLAYER_STAT.BED_LOST)
                .column(PlayerStatTable.PLAYER_STAT.WINS)
                .column(PlayerStatTable.PLAYER_STAT.PLAYED)
                .constraint(DSL.constraint("PK_PLAYER_STATS").primaryKey(PlayerStatTable.PLAYER_STAT.UUID))
                .execute());
    }

    public ISQL.Stage<Integer> createArenaTable() {
        return Stage.of(() -> this.context.createTableIfNotExists(DSL.name(DataTables.ARENA_HISTORY.database()))
                .column(ArenaHistoryTable.ARENA_HISTORY.ID)
                .column(ArenaHistoryTable.ARENA_HISTORY.GAME_ID)
                .column(ArenaHistoryTable.ARENA_HISTORY.TEAM)
                .column(ArenaHistoryTable.ARENA_HISTORY.WINNER)
                .column(ArenaHistoryTable.ARENA_HISTORY.RUNTIME)
                .column(ArenaHistoryTable.ARENA_HISTORY.TIMESTAMP)
                .column(ArenaHistoryTable.ARENA_HISTORY.ITEM_PICKUP)
                .column(ArenaHistoryTable.ARENA_HISTORY.DEATHS)
                .column(ArenaHistoryTable.ARENA_HISTORY.BEDS_BROKEN)
                .execute());
    }

    public ISQL.Stage<Integer> createQuickBuyTable() {
        return Stage.of(() -> this.context.createTableIfNotExists(DSL.name(DataTables.QUICK_BUY.database()))
                .column(QuickBuyTable.QUICK_BUY.UUID)
                .column(QuickBuyTable.QUICK_BUY.NAME)
                .column(QuickBuyTable.QUICK_BUY.QUICK_BUY_DATA)
                .constraint(DSL.constraint("PK_QUICK_BUY").primaryKey(QuickBuyTable.QUICK_BUY.UUID))
                .execute());
    }

}
