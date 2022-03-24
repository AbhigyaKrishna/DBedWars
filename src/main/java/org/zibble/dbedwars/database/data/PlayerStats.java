package org.zibble.dbedwars.database.data;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

import java.util.UUID;

public class PlayerStats implements PlayerDataCache {

    @BsonId
    private UUID uuid;
    private String name;
    private int level;
    private double level_progress;
    private double coins;
    private int winstreak;
    private double points;
    private PersistentStat<Integer> kills;
    private PersistentStat<Integer> final_kills;
    private PersistentStat<Integer> deaths;
    private PersistentStat<Integer> bed_broken;
    private PersistentStat<Integer> bed_lost;
    private PersistentStat<Integer> wins;
    private PersistentStat<Long> played;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getLevelProgress() {
        return level_progress;
    }

    public void setLevelProgress(double level_progress) {
        this.level_progress = level_progress;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public int getWinStreak() {
        return winstreak;
    }

    public void setWinStreak(int winstreak) {
        this.winstreak = winstreak;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
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
        this.uuid = reader.readUUID("uuid");
        this.name = reader.readString("name");
        this.level = reader.readInt("level");
        this.level_progress = reader.readDouble("level_progress");
        this.coins = reader.readDouble("coins");
        this.winstreak = reader.readInt("winstreak");
        this.points = reader.readDouble("points");
        this.kills = reader.readPersistentStat("kills", Integer.class);
        this.final_kills = reader.readPersistentStat("final_kills", Integer.class);
        this.deaths = reader.readPersistentStat("deaths", Integer.class);
        this.bed_broken = reader.readPersistentStat("bed_broken", Integer.class);
        this.bed_lost = reader.readPersistentStat("bed_lost", Integer.class);
        this.wins = reader.readPersistentStat("wins", Integer.class);
        this.played = reader.readPersistentStat("played", Long.class);
    }

    @Override
    public void save(DataWriter<?> writer) throws Exception {
        writer.writeUUID("uuid", this.uuid);
        writer.writeString("name", this.name);
        writer.writeInt("level", this.level);
        writer.writeDouble("level_progress", this.level_progress);
        writer.writeDouble("coins", this.coins);
        writer.writeInt("winstreak", this.winstreak);
        writer.writeDouble("points", this.points);
        writer.writePersistentStat("kills", this.kills);
        writer.writePersistentStat("final_kills", this.final_kills);
        writer.writePersistentStat("deaths", this.deaths);
        writer.writePersistentStat("bed_broken", this.bed_broken);
        writer.writePersistentStat("bed_lost", this.bed_lost);
        writer.writePersistentStat("wins", this.wins);
        writer.writePersistentStat("played", this.played);
    }

    @Override
    public PlayerStats copy() {
        PlayerStats stats = new PlayerStats();
        stats.uuid = this.uuid;
        stats.name = this.name;
        stats.level = this.level;
        stats.level_progress = this.level_progress;
        stats.coins = this.coins;
        stats.winstreak = this.winstreak;
        stats.points = this.points;
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
                .add("uuid", this.uuid)
                .add("name", this.name)
                .add("level", this.level)
                .add("level_progress", this.level_progress)
                .add("coins", this.coins)
                .add("winstreak", this.winstreak)
                .add("points", this.points)
                .add("kills", this.kills.toProperties())
                .add("final_kills", this.final_kills.toProperties())
                .add("deaths", this.deaths.toProperties())
                .add("bed_broken", this.bed_broken.toProperties())
                .add("bed_lost", this.bed_lost.toProperties())
                .add("wins", this.wins.toProperties())
                .add("played", this.played.toProperties())
                .build();
    }

}
