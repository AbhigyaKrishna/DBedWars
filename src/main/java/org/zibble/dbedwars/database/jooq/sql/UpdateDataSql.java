package org.zibble.dbedwars.database.jooq.sql;

import org.jooq.DSLContext;
import org.zibble.dbedwars.database.data.PlayerStats;
import org.zibble.dbedwars.database.data.QuickBuy;
import org.zibble.dbedwars.database.jooq.records.PlayerStatRecord;
import org.zibble.dbedwars.database.jooq.records.QuickBuyRecord;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;
import org.zibble.dbedwars.database.jooq.tables.QuickBuyTable;

public class UpdateDataSql implements ISQL {

    private final DSLContext dsl;

    public UpdateDataSql(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public DSLContext context() {
        return this.dsl;
    }

    public Stage<Boolean> updatePlayerStat(PlayerStats stat) {
        return Stage.of(() -> this.dsl.update(PlayerStatTable.PLAYER_STAT)
//                .set(PlayerStatTable.PLAYER_STAT.UUID, stat.getUuid())
//                .set(PlayerStatTable.PLAYER_STAT.NAME, stat.getName())
//                .set(PlayerStatTable.PLAYER_STAT.LEVEL, stat.getLevel().get())
//                .set(PlayerStatTable.PLAYER_STAT.LEVEL_PROGRESS, stat.getLevelProgress().get())
//                .set(PlayerStatTable.PLAYER_STAT.COINS, stat.getCoins().get())
//                .set(PlayerStatTable.PLAYER_STAT.WINSTREAK, stat.getWinStreak().shortValue())
//                .set(PlayerStatTable.PLAYER_STAT.POINTS, stat.getPoints().get())
//                .set(PlayerStatTable.PLAYER_STAT.KILLS, stat.getKills())
//                .set(PlayerStatTable.PLAYER_STAT.FINAL_KILLS, stat.getFinalKills())
//                .set(PlayerStatTable.PLAYER_STAT.DEATHS, stat.getDeaths())
//                .set(PlayerStatTable.PLAYER_STAT.BED_BROKEN, stat.getBedBroken())
//                .set(PlayerStatTable.PLAYER_STAT.BED_LOST, stat.getBedLost())
//                .set(PlayerStatTable.PLAYER_STAT.WINS, stat.getWins())
//                .set(PlayerStatTable.PLAYER_STAT.PLAYED, stat.getPlayed())
                .set(new PlayerStatRecord(stat))
                .where(PlayerStatTable.PLAYER_STAT.UUID.eq(stat.getUuid()))
                .execute() > 0);
    }

    public Stage<Boolean> updateQuickBuy(QuickBuy quickBuy) {
        return Stage.of(() -> this.dsl.update(QuickBuyTable.QUICK_BUY)
//                .set(QuickBuyTable.QUICK_BUY.UUID, quickBuy.getUuid())
//                .set(QuickBuyTable.QUICK_BUY.NAME, quickBuy.getName())
//                .set(QuickBuyTable.QUICK_BUY.QUICK_BUY_DATA, quickBuy.getData())
                .set(new QuickBuyRecord(quickBuy))
                .where(QuickBuyTable.QUICK_BUY.UUID.eq(quickBuy.getUuid()))
                .execute() > 0);
    }

}
