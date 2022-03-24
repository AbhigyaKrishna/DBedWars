package org.zibble.dbedwars.database.jooq.sql;

import org.jooq.DSLContext;
import org.zibble.dbedwars.database.jooq.records.ArenaHistoryRecord;
import org.zibble.dbedwars.database.jooq.records.PlayerStatRecord;
import org.zibble.dbedwars.database.jooq.records.QuickBuyRecord;
import org.zibble.dbedwars.database.jooq.tables.ArenaHistoryTable;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;
import org.zibble.dbedwars.database.jooq.tables.QuickBuyTable;

import java.util.UUID;

public class InsertDataSQL implements ISQL {

    private final DSLContext dsl;

    public InsertDataSQL(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public DSLContext context() {
        return this.dsl;
    }

    public Stage<Boolean> insertNewPlayerStat(UUID uuid, String name) {
        return Stage.of(() -> this.dsl.insertInto(PlayerStatTable.PLAYER_STAT)
                .set(PlayerStatRecord.newRecord(uuid, name))
                .onDuplicateKeyIgnore()
                .execute() > 0);
    }

    public Stage<Boolean> insertNewQuickBuy(UUID uuid, String name) {
        return Stage.of(() -> this.dsl.insertInto(QuickBuyTable.QUICK_BUY)
                .set(QuickBuyRecord.newRecord(uuid, name))
                .onDuplicateKeyIgnore()
                .execute() > 0);
    }

    public Stage<Boolean> insertNewArenaHistory(ArenaHistoryRecord record) {
        return Stage.of(() -> this.dsl.insertInto(ArenaHistoryTable.ARENA_HISTORY)
                .set(record)
                .execute() > 0);
    }

}
