package org.zibble.dbedwars.cache;

public class PlayerStats implements DataCache {

    private String UUID;
    private String NAME;
    private int LEVEL;
    private double LEVEL_PROGRESS;
    private double COINS;
    private int WINSTREAK;
    private double POINTS;

    public PlayerStats(String UUID, String NAME) {
        this.UUID = UUID;
        this.NAME = NAME;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getLEVEL() {
        return LEVEL;
    }

    public void setLEVEL(int LEVEL) {
        this.LEVEL = LEVEL;
    }

    public double getLEVEL_PROGRESS() {
        return LEVEL_PROGRESS;
    }

    public void setLEVEL_PROGRESS(double LEVEL_PROGRESS) {
        this.LEVEL_PROGRESS = LEVEL_PROGRESS;
    }

    public double getCOINS() {
        return COINS;
    }

    public void setCOINS(double COINS) {
        this.COINS = COINS;
    }

    public int getWINSTREAK() {
        return WINSTREAK;
    }

    public void setWINSTREAK(int WINSTREAK) {
        this.WINSTREAK = WINSTREAK;
    }

    public double getPOINTS() {
        return POINTS;
    }

    public void setPOINTS(double POINTS) {
        this.POINTS = POINTS;
    }
}
