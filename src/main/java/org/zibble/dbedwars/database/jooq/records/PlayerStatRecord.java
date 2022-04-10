package org.zibble.dbedwars.database.jooq.records;

import org.jooq.impl.UpdatableRecordImpl;
import org.zibble.dbedwars.database.data.PersistentStat;
import org.zibble.dbedwars.database.data.PlayerStats;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;

import java.util.UUID;

public class PlayerStatRecord extends UpdatableRecordImpl<PlayerStatRecord> {

    public PlayerStatRecord() {
        super(PlayerStatTable.PLAYER_STAT);
    }

    public static PlayerStatRecord newRecord(UUID uuid, String name) {
        PlayerStatTable table = PlayerStatTable.PLAYER_STAT;
        PlayerStatRecord record = new PlayerStatRecord();
        record.set(table.UUID, uuid);
        record.set(table.NAME, name);
        record.set(table.LEVEL, 0);
        record.set(table.LEVEL_PROGRESS, 0.0);
        record.set(table.COINS, 0.0);
        record.set(table.WINSTREAK, (short) 0);
        record.set(table.POINTS, 0.0);
        record.set(table.KILLS, new PersistentStat<>("kills"));
        record.set(table.FINAL_KILLS, new PersistentStat<>("final_kills"));
        record.set(table.DEATHS, new PersistentStat<>("deaths"));
        record.set(table.BED_BROKEN, new PersistentStat<>("bed_broken"));
        record.set(table.BED_LOST, new PersistentStat<>("bed_lost"));
        record.set(table.WINS, new PersistentStat<>("wins"));
        record.set(table.PLAYED, new PersistentStat<>("played"));
        return record;
    }

    public static PlayerStatRecord fromDataCache(PlayerStats data) {
        PlayerStatRecord record = new PlayerStatRecord();
        PlayerStatTable table = (PlayerStatTable) record.getTable();
        record.set(table.UUID, data.getUuid());
        record.set(table.NAME, data.getName());
        record.set(table.LEVEL, data.getLevel());
        record.set(table.LEVEL_PROGRESS, data.getLevelProgress());
        record.set(table.COINS, data.getCoins());
        record.set(table.WINSTREAK, (short) data.getWinStreak());
        record.set(table.POINTS, data.getPoints());
        record.set(table.KILLS, data.getKills());
        record.set(table.FINAL_KILLS, data.getFinalKills());
        record.set(table.DEATHS, data.getDeaths());
        record.set(table.BED_BROKEN, data.getBedBroken());
        record.set(table.BED_LOST, data.getBedLost());
        record.set(table.WINS, data.getWins());
        record.set(table.PLAYED, data.getPlayed());
        return record;
    }

    public PlayerStats toDataCache() {
        PlayerStatTable table = (PlayerStatTable) this.getTable();
        PlayerStats data = new PlayerStats();
        data.setUuid(this.get(table.UUID));
        data.setName(this.get(table.NAME));
        data.setLevel(this.get(table.LEVEL));
        data.setLevelProgress(this.get(table.LEVEL_PROGRESS));
        data.setCoins(this.get(table.COINS));
        data.setWinStreak(this.get(table.WINSTREAK));
        data.setPoints(this.get(table.POINTS));
        data.setKills(this.get(table.KILLS));
        data.setFinalKills(this.get(table.FINAL_KILLS));
        data.setDeaths(this.get(table.DEATHS));
        data.setBedBroken(this.get(table.BED_BROKEN));
        data.setBedLost(this.get(table.BED_LOST));
        data.setWins(this.get(table.WINS));
        data.setPlayed(this.get(table.PLAYED));
        return data;
    }

}
