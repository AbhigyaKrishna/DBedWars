package org.zibble.dbedwars.database.data;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.zibble.dbedwars.api.objects.points.Count;
import org.zibble.dbedwars.api.objects.points.DoubleCount;
import org.zibble.dbedwars.api.objects.points.IntegerCount;
import org.zibble.dbedwars.api.objects.points.LongCount;
import org.zibble.dbedwars.api.util.DataType;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

import java.util.UUID;

public class PlayerStats implements PlayerDataCache {

    public static final Key UUID = Key.of("uuid");
    public static final Key NAME = Key.of("name");
    public static final Key LEVEL = Key.of("level");
    public static final Key LEVEL_PROGRESS = Key.of("level_progress");
    public static final Key COINS = Key.of("coins");
    public static final Key WINSTREAK = Key.of("winstreak");
    public static final Key POINTS = Key.of("points");
    public static final Key KILLS = Key.of("kills");
    public static final Key FINAL_KILLS = Key.of("final_kills");
    public static final Key DEATHS = Key.of("deaths");
    public static final Key BED_BROKEN = Key.of("bed_broken");
    public static final Key BED_LOST = Key.of("bed_lost");
    public static final Key WIN = Key.of("win");
    public static final Key PLAYED = Key.of("played");


    @BsonId
    private UUID uuid;
    private String name;
    private Count<Integer> level = new IntegerCount();
    private Count<Double> level_progress = new DoubleCount();
    private Count<Double> coins = new DoubleCount();
    private Count<Integer> winstreak = new IntegerCount();
    private Count<Double> points = new DoubleCount();
    private PersistentStat<Integer> kills = new PersistentStat<>(KILLS, IntegerCount::new);
    private PersistentStat<Integer> final_kills = new PersistentStat<>(FINAL_KILLS, IntegerCount::new);
    private PersistentStat<Integer> deaths = new PersistentStat<>(DEATHS, IntegerCount::new);
    private PersistentStat<Integer> bed_broken = new PersistentStat<>(BED_BROKEN, IntegerCount::new);
    private PersistentStat<Integer> bed_lost = new PersistentStat<>(BED_LOST, IntegerCount::new);
    private PersistentStat<Integer> wins = new PersistentStat<>(WIN, IntegerCount::new);
    private PersistentStat<Long> played = new PersistentStat<>(PLAYED, LongCount::new);

    @BsonCreator
    public PlayerStats() {
    }

    public PlayerStats(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Count<Integer> getLevel() {
        return level;
    }

    public void setLevel(Count<Integer> level) {
        this.level = level;
    }

    public Count<Double> getLevelProgress() {
        return level_progress;
    }

    public void setLevelProgress(Count<Double> level_progress) {
        this.level_progress = level_progress;
    }

    public Count<Double> getCoins() {
        return coins;
    }

    public void setCoins(Count<Double> coins) {
        this.coins = coins;
    }

    public Count<Integer> getWinStreak() {
        return winstreak;
    }

    public void setWinStreak(Count<Integer> winstreak) {
        this.winstreak = winstreak;
    }

    public Count<Double> getPoints() {
        return points;
    }

    public void setPoints(Count<Double> points) {
        this.points = points;
    }

    public PersistentStat<Integer> getKills() {
        return kills;
    }

    public void setKills(PersistentStat<Integer> kills) {
        this.kills = kills;
    }

    public PersistentStat<Integer> getFinalKills() {
        return final_kills;
    }

    public void setFinalKills(PersistentStat<Integer> final_kills) {
        this.final_kills = final_kills;
    }

    public PersistentStat<Integer> getDeaths() {
        return deaths;
    }

    public void setDeaths(PersistentStat<Integer> deaths) {
        this.deaths = deaths;
    }

    public PersistentStat<Integer> getBedBroken() {
        return bed_broken;
    }

    public void setBedBroken(PersistentStat<Integer> bed_broken) {
        this.bed_broken = bed_broken;
    }

    public PersistentStat<Integer> getBedLost() {
        return bed_lost;
    }

    public void setBedLost(PersistentStat<Integer> bed_lost) {
        this.bed_lost = bed_lost;
    }

    public PersistentStat<Integer> getWins() {
        return wins;
    }

    public void setWins(PersistentStat<Integer> wins) {
        this.wins = wins;
    }

    public PersistentStat<Long> getPlayed() {
        return played;
    }

    public void setPlayed(PersistentStat<Long> played) {
        this.played = played;
    }

    @Override
    public void load(DataReader<?> reader) throws Exception {
        this.uuid = reader.readUUID(UUID);
        this.name = reader.readString(NAME);
        this.level = reader.readCount(LEVEL, DataType.INTEGER);
        this.level_progress = reader.readCount(LEVEL_PROGRESS, DataType.DOUBLE);
        this.coins = reader.readCount(COINS, DataType.DOUBLE);
        this.winstreak = reader.readCount(WINSTREAK, DataType.INTEGER);
        this.points = reader.readCount(POINTS, DataType.DOUBLE);
        this.kills = reader.readPersistentStat(KILLS, Integer.class);
        this.final_kills = reader.readPersistentStat(FINAL_KILLS, Integer.class);
        this.deaths = reader.readPersistentStat(DEATHS, Integer.class);
        this.bed_broken = reader.readPersistentStat(BED_BROKEN, Integer.class);
        this.bed_lost = reader.readPersistentStat(BED_LOST, Integer.class);
        this.wins = reader.readPersistentStat(WIN, Integer.class);
        this.played = reader.readPersistentStat(PLAYED, Long.class);
    }

    @Override
    public void save(DataWriter<?> writer) throws Exception {
        writer.writeUUID(UUID, this.uuid);
        writer.writeString(NAME, this.name);
        writer.writeCount(LEVEL, this.level);
        writer.writeCount(LEVEL_PROGRESS, this.level_progress);
        writer.writeCount(COINS, this.coins);
        writer.writeCount(WINSTREAK, this.winstreak);
        writer.writeCount(POINTS, this.points);
        writer.writePersistentStat(KILLS, this.kills);
        writer.writePersistentStat(FINAL_KILLS, this.final_kills);
        writer.writePersistentStat(DEATHS, this.deaths);
        writer.writePersistentStat(BED_BROKEN, this.bed_broken);
        writer.writePersistentStat(BED_LOST, this.bed_lost);
        writer.writePersistentStat(WIN, this.wins);
        writer.writePersistentStat(PLAYED, this.played);
    }

    @Override
    public PlayerStats copy() {
        PlayerStats stats = new PlayerStats();
        stats.uuid = this.uuid;
        stats.name = this.name;
        stats.level = this.level.clone();
        stats.level_progress = this.level_progress.clone();
        stats.coins = this.coins.clone();
        stats.winstreak = this.winstreak.clone();
        stats.points = this.points.clone();
        stats.kills = this.kills.copy();
        stats.final_kills = this.final_kills.copy();
        stats.deaths = this.deaths.copy();
        stats.bed_broken = this.bed_broken.copy();
        stats.bed_lost = this.bed_lost.copy();
        stats.wins = this.wins.copy();
        stats.played = this.played.copy();
        return stats;
    }

    @Override
    public NamedProperties toProperties() {
        return NamedProperties.builder()
                .add(UUID, this.uuid)
                .add(NAME, this.name)
                .add(LEVEL, this.level.get())
                .add(LEVEL_PROGRESS, this.level_progress.get())
                .add(COINS, this.coins.get())
                .add(WINSTREAK, this.winstreak.get())
                .add(POINTS, this.points.get())
                .add(KILLS, this.kills.toProperties())
                .add(FINAL_KILLS, this.final_kills.toProperties())
                .add(DEATHS, this.deaths.toProperties())
                .add(BED_BROKEN, this.bed_broken.toProperties())
                .add(BED_LOST, this.bed_lost.toProperties())
                .add(WIN, this.wins.toProperties())
                .add(PLAYED, this.played.toProperties())
                .build();
    }

}
