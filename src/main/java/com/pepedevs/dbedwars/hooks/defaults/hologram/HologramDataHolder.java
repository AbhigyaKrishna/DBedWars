package com.pepedevs.dbedwars.hooks.defaults.hologram;

public class HologramDataHolder {

    private final HologramImpl hologram;
    private boolean isClickRegistered = true;
    private boolean isUpdateRegistered = true;
    private long lastUpdateTime = System.currentTimeMillis();
    private boolean isSpawned =  true;

    protected HologramDataHolder(HologramImpl hologram) {
        this.hologram = hologram;
    }

    protected HologramDataHolder(HologramImpl hologram, boolean isSpawned) {
        this.hologram = hologram;
        this.isSpawned = isSpawned;
    }

    public HologramImpl getHologram() {
        return hologram;
    }

    public boolean isClickRegistered() {
        return isClickRegistered;
    }

    public void setClickRegistered(boolean clickRegistered) {
        isClickRegistered = clickRegistered;
    }

    public boolean isUpdateRegistered() {
        return isUpdateRegistered;
    }

    public void setUpdateRegistered(boolean updateRegistered) {
        isUpdateRegistered = updateRegistered;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isSpawned() {
        return isSpawned;
    }

    public void setSpawned(boolean spawned) {
        isSpawned = spawned;
    }
}
