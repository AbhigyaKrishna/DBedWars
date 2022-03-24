package org.zibble.dbedwars.database.jooq.sql;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.zibble.dbedwars.database.data.ArenaHistory;
import org.zibble.dbedwars.database.data.PlayerStats;
import org.zibble.dbedwars.database.data.QuickBuy;
import org.zibble.dbedwars.database.jooq.records.ArenaHistoryRecord;
import org.zibble.dbedwars.database.jooq.records.PlayerStatRecord;
import org.zibble.dbedwars.database.jooq.records.QuickBuyRecord;
import org.zibble.dbedwars.database.jooq.tables.ArenaHistoryTable;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;
import org.zibble.dbedwars.database.jooq.tables.QuickBuyTable;

import java.util.Collection;
import java.util.UUID;

public class FetchDataSQL implements ISQL {

    private final DSLContext dsl;

    public FetchDataSQL(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public DSLContext context() {
        return this.dsl;
    }

    public Stage<PlayerStats> fetchPlayerStat(UUID uuid) {
        return Stage.of(() -> this.dsl.fetchOne(PlayerStatTable.PLAYER_STAT, PlayerStatTable.PLAYER_STAT.UUID.eq(uuid)).toDataCache());
    }

    public Stage<Collection<PlayerStats>> fetchPlayerStat(Condition condition) {
        return Stage.of(() -> this.dsl.fetch(PlayerStatTable.PLAYER_STAT, condition).map(PlayerStatRecord::toDataCache));
    }

    public Stage<QuickBuy> fetchQuickBuy(UUID uuid) {
        return Stage.of(() -> this.dsl.fetchOne(QuickBuyTable.QUICK_BUY, QuickBuyTable.QUICK_BUY.UUID.eq(uuid)).toDataCache());
    }

    public Stage<Collection<QuickBuy>> fetchQuickBuy(Condition condition) {
        return Stage.of(() -> this.dsl.fetch(QuickBuyTable.QUICK_BUY, condition).map(QuickBuyRecord::toDataCache));
    }

    public Stage<Collection<ArenaHistory>> fetchHistory(Condition condition) {
        return Stage.of(() -> this.dsl.fetch(ArenaHistoryTable.ARENA_HISTORY, condition).map(ArenaHistoryRecord::toDataCache));
    }

}
