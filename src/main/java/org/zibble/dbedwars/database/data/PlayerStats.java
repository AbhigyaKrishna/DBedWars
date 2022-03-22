package org.zibble.dbedwars.database.data;

import org.zibble.dbedwars.api.util.properies.NamedProperties;
import org.zibble.dbedwars.database.data.io.DataReader;
import org.zibble.dbedwars.database.data.io.DataWriter;

import java.util.UUID;

public class PlayerStats implements PlayerDataCache {

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
    }

    @Override
    public void save(DataWriter<?> writer) throws Exception {
        writer.writeUUID("uuid", uuid);
        writer.writeString("name", name);
        writer.writeInt("level", level);
        writer.writeDouble("level_progress", level_progress);
        writer.writeDouble("coins", coins);
        writer.writeInt("winstreak", winstreak);
        writer.writeDouble("points", points);
    }

    @Override
    public PlayerStats copy() {
        PlayerStats stats = new PlayerStats();
        stats.uuid = uuid;
        stats.name = name;
        stats.level = level;
        stats.level_progress = level_progress;
        stats.coins = coins;
        stats.winstreak = winstreak;
        stats.points = points;
        return stats;
    }

    @Override
    public NamedProperties toProperties() {
        return NamedProperties.builder()
                .add("uuid", uuid.toString())
                .add("name", name)
                .add("level", level)
                .add("level_progress", level_progress)
                .add("coins", coins)
                .add("winstreak", winstreak)
                .add("points", points)
                .build();
    }

}
