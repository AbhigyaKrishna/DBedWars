package me.abhigya.dbedwars.cache;

public class PlayerStats implements DataCache {

    private String UUID;
    private String NAME;
    private int LEVEL;
    private double LEVEL_PROGRESS;
    private double COINS;
    private int WINSTREAK;
    private double POINTS;

    public PlayerStats( String UUID, String NAME ) {
        this.UUID = UUID;
        this.NAME = NAME;
    }

    public String getUUID( ) {
        return UUID;
    }

    public String getNAME( ) {
        return NAME;
    }

    public int getLEVEL( ) {
        return LEVEL;
    }

    public double getLEVEL_PROGRESS( ) {
        return LEVEL_PROGRESS;
    }

    public double getCOINS( ) {
        return COINS;
    }

    public int getWINSTREAK( ) {
        return WINSTREAK;
    }

    public double getPOINTS( ) {
        return POINTS;
    }

}
