package org.zibble.dbedwars.database.jooq.sql;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;

import java.sql.Timestamp;
import java.time.Instant;

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
        return Stage.of(() -> this.context.createTableIfNotExists(DSL.name(Table.PLAYER_STATS))
                .column(Table.PlayerStats.UUID)
                .column(Table.PlayerStats.NAME)
                .column(Table.PlayerStats.LEVEL)
                .column(Table.PlayerStats.LEVEL_PROGRESS)
                .column(Table.PlayerStats.COINS)
                .column(Table.PlayerStats.WINSTREAK)
                .column(Table.PlayerStats.POINTS)
                .column(Table.PlayerStats.KILLS)
                .column(Table.PlayerStats.FINAL_KILLS)
                .column(Table.PlayerStats.DEATHS)
                .column(Table.PlayerStats.BED_BROKEN)
                .column(Table.PlayerStats.BED_LOST)
                .column(Table.PlayerStats.WINS)
                .column(Table.PlayerStats.PLAYED)
                .primaryKey(Table.PlayerStats.UUID)
                .execute());
    }

    public ISQL.Stage<Integer> createArenaTable() {
        return Stage.of(() -> this.context.createTableIfNotExists(DSL.name(Table.ARENA))
                .column(Table.Arena.ID)
                .column(Table.Arena.GAME_ID)
                .column(Table.Arena.TEAMS)
                .column(Table.Arena.WINNER)
                .column(Table.Arena.TIMESTAMP)
                .column(Table.Arena.ITEM_PICKUP)
                .column(Table.Arena.DEATHS)
                .column(Table.Arena.BEDS_BROKEN)
                .execute());
    }

    public ISQL.Stage<Integer> createQuickBuyTable() {
        return Stage.of(() -> this.context.createTableIfNotExists(DSL.name(Table.QUICK_BUY))
                .column(Table.QuickBuy.UUID)
                .column(Table.QuickBuy.NAME)
                .column(Table.QuickBuy.DATA)
                .primaryKey(Table.QuickBuy.UUID)
                .execute());
    }

    private static class Table {
        public static final String PLAYER_STATS = "dbedwars_player_stats";
        public static final String ARENA = "dbedwars_arena_history";
        public static final String QUICK_BUY = "dbedwars_quick_buy";

        static class PlayerStats {
            

            public static final Field<?> UUID = DSL.field(DSL.name("UUID"), SQLDataType.UUID.notNull());
            public static final Field<?> NAME = DSL.field(DSL.name("NAME"), SQLDataType.VARCHAR(16).notNull());
            public static final Field<?> LEVEL = DSL.field(DSL.name("LEVEL"), SQLDataType.INTEGER.notNull().defaultValue(0));
            public static final Field<?> LEVEL_PROGRESS = DSL.field(DSL.name("LEVEL_PROGRESS"), SQLDataType.DOUBLE.notNull().defaultValue(0.0));
            public static final Field<?> COINS = DSL.field(DSL.name("COINS"), SQLDataType.DOUBLE.notNull().defaultValue(0.0));
            public static final Field<?> WINSTREAK = DSL.field(DSL.name("WINSTREAK"), SQLDataType.SMALLINT.notNull().defaultValue((short) 0));
            public static final Field<?> POINTS = DSL.field(DSL.name("POINTS"), SQLDataType.DOUBLE.notNull().defaultValue(0.0));
            public static final Field<?> KILLS = DSL.field(DSL.name("KILLS"), SQLDataType.JSON.notNull().defaultValue(JSON.valueOf(PlayerStatTable.DEFAULT_JSON)));
            public static final Field<?> FINAL_KILLS = DSL.field(DSL.name("FINAL_KILLS"), SQLDataType.JSON.notNull().defaultValue(JSON.valueOf(PlayerStatTable.DEFAULT_JSON)));
            public static final Field<?> DEATHS = DSL.field(DSL.name("DEATHS"), SQLDataType.JSON.notNull().defaultValue(JSON.valueOf(PlayerStatTable.DEFAULT_JSON)));
            public static final Field<?> BED_BROKEN = DSL.field(DSL.name("BED_BROKEN"), SQLDataType.JSON.notNull().defaultValue(JSON.valueOf(PlayerStatTable.DEFAULT_JSON)));
            public static final Field<?> BED_LOST = DSL.field(DSL.name("BED_LOST"), SQLDataType.JSON.notNull().defaultValue(JSON.valueOf(PlayerStatTable.DEFAULT_JSON)));
            public static final Field<?> WINS = DSL.field(DSL.name("WINS"), SQLDataType.JSON.notNull().defaultValue(JSON.valueOf(PlayerStatTable.DEFAULT_JSON)));
            public static final Field<?> PLAYED = DSL.field(DSL.name("PLAYED"), SQLDataType.JSON.notNull().defaultValue(JSON.valueOf(PlayerStatTable.DEFAULT_JSON)));
        }

        static class Arena {
            public static final Field<?> ID = DSL.field(DSL.name("ID"), SQLDataType.VARCHAR(20).notNull());
            public static final Field<?> GAME_ID = DSL.field(DSL.name("GAME_ID"), SQLDataType.VARCHAR(50).notNull());
            public static final Field<?> TEAMS = DSL.field(DSL.name("TEAMS"), SQLDataType.JSON.notNull());
            public static final Field<?> WINNER = DSL.field(DSL.name("WINNER"), SQLDataType.TINYINT.notNull());
            public static final Field<?> RUNTIME = DSL.field(DSL.name("RUNTIME"), SQLDataType.INTERVAL.notNull());
            public static final Field<?> TIMESTAMP = DSL.field(DSL.name("TIMESTAMP"), SQLDataType.INSTANT.notNull().defaultValue(Instant.now()));
            public static final Field<?> ITEM_PICKUP = DSL.field(DSL.name("ITEM_PICKUP"), SQLDataType.JSON.notNull());
            public static final Field<?> DEATHS = DSL.field(DSL.name("DEATHS"), SQLDataType.JSON.notNull());
            public static final Field<?> BEDS_BROKEN = DSL.field(DSL.name("BEDS_BROKEN"), SQLDataType.JSON.notNull());
        }

        static class QuickBuy {
            public static final Field<?> UUID = DSL.field(DSL.name("UUID"), SQLDataType.UUID.notNull());
            public static final Field<?> NAME = DSL.field(DSL.name("NAME"), SQLDataType.VARCHAR(16).notNull());
            public static final Field<?> DATA = DSL.field(DSL.name("QUICK_BUY_DATA"), SQLDataType.JSON.notNull());
        }
    }

}
