package org.zibble.dbedwars.database.jooq.records;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record14;
import org.jooq.Row14;
import org.jooq.impl.UpdatableRecordImpl;
import org.zibble.dbedwars.api.objects.points.DoubleCount;
import org.zibble.dbedwars.api.objects.points.IntegerCount;
import org.zibble.dbedwars.database.data.PersistentStat;
import org.zibble.dbedwars.database.data.PlayerStats;
import org.zibble.dbedwars.database.jooq.tables.PlayerStatTable;

import java.util.UUID;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class PlayerStatRecord extends UpdatableRecordImpl<PlayerStatRecord> implements Record14<UUID, String, Integer, Double, Double, Short, Double, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Long>> {

    public PlayerStatRecord() {
        super(PlayerStatTable.PLAYER_STAT);
    }

    public PlayerStatRecord(UUID uuid, String name, Integer level, Double levelProgress, Double coins, Short winstreak, Double points, PersistentStat<Integer> kills, PersistentStat<Integer> finalKills, PersistentStat<Integer> deaths, PersistentStat<Integer> bedBroken, PersistentStat<Integer> bedLost, PersistentStat<Integer> wins, PersistentStat<Long> played) {
        super(PlayerStatTable.PLAYER_STAT);

        setUuid(uuid);
        setName(name);
        setLevel(level);
        setLevelProgress(levelProgress);
        setCoins(coins);
        setWinstreak(winstreak);
        setPoints(points);
        setKills(kills);
        setFinalKills(finalKills);
        setDeaths(deaths);
        setBedBroken(bedBroken);
        setBedLost(bedLost);
        setWins(wins);
        setPlayed(played);
    }

    public PlayerStatRecord(PlayerStats stats) {
        super(PlayerStatTable.PLAYER_STAT);

        setUuid(stats.getUuid());
        setName(stats.getName());
        setLevel(stats.getLevel().get());
        setLevelProgress(stats.getLevelProgress().get());
        setCoins(stats.getCoins().get());
        setWinstreak(stats.getWinStreak().shortValue());
        setPoints(stats.getPoints().get());
        setKills(stats.getKills());
        setFinalKills(stats.getFinalKills());
        setDeaths(stats.getDeaths());
        setBedBroken(stats.getBedBroken());
        setBedLost(stats.getBedLost());
        setWins(stats.getWins());
        setPlayed(stats.getPlayed());
    }

    public PlayerStats toDataCache() {
        PlayerStats data = new PlayerStats();
        data.setUuid(this.getUuid());
        data.setName(this.getName());
        data.setLevel(new IntegerCount(this.getLevel()));
        data.setLevelProgress(new DoubleCount(this.getLevelProgress()));
        data.setCoins(new DoubleCount(this.getCoins()));
        data.setWinStreak(new IntegerCount(this.getWinstreak().intValue()));
        data.setPoints(new DoubleCount(this.getPoints()));
        data.setKills(this.getKills());
        data.setFinalKills(this.getFinalKills());
        data.setDeaths(this.getDeaths());
        data.setBedBroken(this.getBedBroken());
        data.setBedLost(this.getBedLost());
        data.setWins(this.getWins());
        data.setPlayed(this.getPlayed());
        return data;
    }

    public void setUuid(UUID value) {
        set(0, value);
    }

    public UUID getUuid() {
        return (UUID) get(0);
    }

    public void setName(String value) {
        set(1, value);
    }

    public String getName() {
        return (String) get(1);
    }

    public void setLevel(Integer value) {
        set(2, value);
    }

    public Integer getLevel() {
        return (Integer) get(2);
    }

    public void setLevelProgress(Double value) {
        set(3, value);
    }

    public Double getLevelProgress() {
        return (Double) get(3);
    }

    public void setCoins(Double value) {
        set(4, value);
    }

    public Double getCoins() {
        return (Double) get(4);
    }

    public void setWinstreak(Short value) {
        set(5, value);
    }

    public Short getWinstreak() {
        return (Short) get(5);
    }

    public void setPoints(Double value) {
        set(6, value);
    }

    public Double getPoints() {
        return (Double) get(6);
    }

    public void setKills(PersistentStat<Integer> value) {
        set(7, value);
    }

    public PersistentStat<Integer> getKills() {
        return (PersistentStat<Integer>) get(7);
    }

    public void setFinalKills(PersistentStat<Integer> value) {
        set(8, value);
    }

    public PersistentStat<Integer> getFinalKills() {
        return (PersistentStat<Integer>) get(8);
    }

    public void setDeaths(PersistentStat<Integer> value) {
        set(9, value);
    }

    public PersistentStat<Integer> getDeaths() {
        return (PersistentStat<Integer>) get(9);
    }

    public void setBedBroken(PersistentStat<Integer> value) {
        set(10, value);
    }

    public PersistentStat<Integer> getBedBroken() {
        return (PersistentStat<Integer>) get(10);
    }

    public void setBedLost(PersistentStat<Integer> value) {
        set(11, value);
    }

    public PersistentStat<Integer> getBedLost() {
        return (PersistentStat<Integer>) get(11);
    }

    public void setWins(PersistentStat<Integer> value) {
        set(12, value);
    }

    public PersistentStat<Integer> getWins() {
        return (PersistentStat<Integer>) get(12);
    }

    public void setPlayed(PersistentStat<Long> value) {
        set(13, value);
    }

    public PersistentStat<Long> getPlayed() {
        return (PersistentStat<Long>) get(13);
    }
    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    @Override
    public Row14<UUID, String, Integer, Double, Double, Short, Double, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Long>> fieldsRow() {
        return (Row14) super.fieldsRow();
    }

    @Override
    public Row14<UUID, String, Integer, Double, Double, Short, Double, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Integer>, PersistentStat<Long>> valuesRow() {
        return (Row14) super.valuesRow();
    }

    @Override
    public Field<UUID> field1() {
        return PlayerStatTable.PLAYER_STAT.UUID;
    }

    @Override
    public Field<String> field2() {
        return PlayerStatTable.PLAYER_STAT.NAME;
    }

    @Override
    public Field<Integer> field3() {
        return PlayerStatTable.PLAYER_STAT.LEVEL;
    }

    @Override
    public Field<Double> field4() {
        return PlayerStatTable.PLAYER_STAT.LEVEL_PROGRESS;
    }

    @Override
    public Field<Double> field5() {
        return PlayerStatTable.PLAYER_STAT.COINS;
    }

    @Override
    public Field<Short> field6() {
        return PlayerStatTable.PLAYER_STAT.WINSTREAK;
    }

    @Override
    public Field<Double> field7() {
        return PlayerStatTable.PLAYER_STAT.POINTS;
    }

    @Override
    public Field<PersistentStat<Integer>> field8() {
        return PlayerStatTable.PLAYER_STAT.KILLS;
    }

    @Override
    public Field<PersistentStat<Integer>> field9() {
        return PlayerStatTable.PLAYER_STAT.FINAL_KILLS;
    }

    @Override
    public Field<PersistentStat<Integer>> field10() {
        return PlayerStatTable.PLAYER_STAT.DEATHS;
    }

    @Override
    public Field<PersistentStat<Integer>> field11() {
        return PlayerStatTable.PLAYER_STAT.BED_BROKEN;
    }

    @Override
    public Field<PersistentStat<Integer>> field12() {
        return PlayerStatTable.PLAYER_STAT.BED_LOST;
    }

    @Override
    public Field<PersistentStat<Integer>> field13() {
        return PlayerStatTable.PLAYER_STAT.WINS;
    }

    @Override
    public Field<PersistentStat<Long>> field14() {
        return PlayerStatTable.PLAYER_STAT.PLAYED;
    }

    @Override
    public UUID component1() {
        return getUuid();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public Integer component3() {
        return getLevel();
    }

    @Override
    public Double component4() {
        return getLevelProgress();
    }

    @Override
    public Double component5() {
        return getCoins();
    }

    @Override
    public Short component6() {
        return getWinstreak();
    }

    @Override
    public Double component7() {
        return getPoints();
    }

    @Override
    public PersistentStat<Integer> component8() {
        return getKills();
    }

    @Override
    public PersistentStat<Integer> component9() {
        return getFinalKills();
    }

    @Override
    public PersistentStat<Integer> component10() {
        return getDeaths();
    }

    @Override
    public PersistentStat<Integer> component11() {
        return getBedBroken();
    }

    @Override
    public PersistentStat<Integer> component12() {
        return getBedLost();
    }

    @Override
    public PersistentStat<Integer> component13() {
        return getWins();
    }

    @Override
    public PersistentStat<Long> component14() {
        return getPlayed();
    }

    @Override
    public UUID value1() {
        return getUuid();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public Integer value3() {
        return getLevel();
    }

    @Override
    public Double value4() {
        return getLevelProgress();
    }

    @Override
    public Double value5() {
        return getCoins();
    }

    @Override
    public Short value6() {
        return getWinstreak();
    }

    @Override
    public Double value7() {
        return getPoints();
    }

    @Override
    public PersistentStat<Integer> value8() {
        return getKills();
    }

    @Override
    public PersistentStat<Integer> value9() {
        return getFinalKills();
    }

    @Override
    public PersistentStat<Integer> value10() {
        return getDeaths();
    }

    @Override
    public PersistentStat<Integer> value11() {
        return getBedBroken();
    }

    @Override
    public PersistentStat<Integer> value12() {
        return getBedLost();
    }

    @Override
    public PersistentStat<Integer> value13() {
        return getWins();
    }

    @Override
    public PersistentStat<Long> value14() {
        return getPlayed();
    }

    @Override
    public PlayerStatRecord value1(UUID value) {
        setUuid(value);
        return this;
    }

    @Override
    public PlayerStatRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public PlayerStatRecord value3(Integer value) {
        setLevel(value);
        return this;
    }

    @Override
    public PlayerStatRecord value4(Double value) {
        setLevelProgress(value);
        return this;
    }

    @Override
    public PlayerStatRecord value5(Double value) {
        setCoins(value);
        return this;
    }

    @Override
    public PlayerStatRecord value6(Short value) {
        setWinstreak(value);
        return this;
    }

    @Override
    public PlayerStatRecord value7(Double value) {
        setPoints(value);
        return this;
    }

    @Override
    public PlayerStatRecord value8(PersistentStat<Integer> value) {
        setKills(value);
        return this;
    }

    @Override
    public PlayerStatRecord value9(PersistentStat<Integer> value) {
        setFinalKills(value);
        return this;
    }

    @Override
    public PlayerStatRecord value10(PersistentStat<Integer> value) {
        setDeaths(value);
        return this;
    }

    @Override
    public PlayerStatRecord value11(PersistentStat<Integer> value) {
        setBedBroken(value);
        return this;
    }

    @Override
    public PlayerStatRecord value12(PersistentStat<Integer> value) {
        setBedLost(value);
        return this;
    }

    @Override
    public PlayerStatRecord value13(PersistentStat<Integer> value) {
        setWins(value);
        return this;
    }

    @Override
    public PlayerStatRecord value14(PersistentStat<Long> value) {
        setPlayed(value);
        return this;
    }

    @Override
    public PlayerStatRecord values(UUID value1, String value2, Integer value3, Double value4, Double value5, Short value6, Double value7, PersistentStat<Integer> value8, PersistentStat<Integer> value9, PersistentStat<Integer> value10, PersistentStat<Integer> value11, PersistentStat<Integer> value12, PersistentStat<Integer> value13, PersistentStat<Long> value14) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        return this;
    }

}
