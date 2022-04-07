package org.zibble.dbedwars.game.arena.settings;

import org.zibble.dbedwars.api.game.settings.ArenaSettings;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.properies.NamedProperties;

public class ArenaSettingsImpl implements ArenaSettings {

    /* Overridable settings */
    private int startTimer;
    private int respawnTime;
    private int islandRadius;
    private int minYAxis;
    private int playerHitTagLength;
    private int gameEndDelay;
    private boolean disableHunger;
    // Points
    private int bedDestroyPoint;
    private int killPoint;
    private int finalKillPoint;
    private int deathPoint;

    private Duration trapTriggerDelay;
    private boolean trapQueueEnabled;
    private int trapQueueSize;

    public ArenaSettingsImpl() {
    }

    public ArenaSettingsImpl(NamedProperties properties) {
        this.startTimer = properties.getValue("startTimer");
        this.respawnTime = properties.getValue("respawnTime");
        this.islandRadius = properties.getValue("islandRadius");
        this.minYAxis = properties.getValue("minYAxis");
        this.playerHitTagLength = properties.getValue("playerHitTagLength");
        this.gameEndDelay = properties.getValue("gameEndDelay");
        this.disableHunger = properties.getValue("disableHunger", true);
        this.bedDestroyPoint = properties.getValue("bedDestroyPoint");
        this.killPoint = properties.getValue("killPoint");
        this.finalKillPoint = properties.getValue("finalKillPoint");
        this.deathPoint = properties.getValue("deathPoint");
        this.trapTriggerDelay = properties.getValue("trapTriggerDelay");
        this.trapQueueEnabled = properties.getValue("trapQueueEnabled", false);
        this.trapQueueSize = properties.getValue("trapQueueSize");
    }

    @Override
    public int getStartTimer() {
        return startTimer;
    }

    @Override
    public void setStartTimer(int startTimer) {
        this.startTimer = startTimer;
    }

    @Override
    public int getRespawnTime() {
        return respawnTime;
    }

    @Override
    public void setRespawnTime(int respawnTime) {
        this.respawnTime = respawnTime;
    }

    @Override
    public int getIslandRadius() {
        return islandRadius;
    }

    @Override
    public void setIslandRadius(int islandRadius) {
        this.islandRadius = islandRadius;
    }

    @Override
    public int getMinYAxis() {
        return minYAxis;
    }

    @Override
    public void setMinYAxis(int minYAxis) {
        this.minYAxis = minYAxis;
    }

    @Override
    public int getPlayerHitTagLength() {
        return playerHitTagLength;
    }

    @Override
    public void setPlayerHitTagLength(int playerHitTagLength) {
        this.playerHitTagLength = playerHitTagLength;
    }

    @Override
    public int getGameEndDelay() {
        return gameEndDelay;
    }

    @Override
    public void setGameEndDelay(int gameEndDelay) {
        this.gameEndDelay = gameEndDelay;
    }

    @Override
    public boolean isDisableHunger() {
        return disableHunger;
    }

    @Override
    public void setDisableHunger(boolean disableHunger) {
        this.disableHunger = disableHunger;
    }

    @Override
    public int getBedDestroyPoint() {
        return bedDestroyPoint;
    }

    @Override
    public void setBedDestroyPoint(int bedDestroyPoint) {
        this.bedDestroyPoint = bedDestroyPoint;
    }

    @Override
    public int getKillPoint() {
        return killPoint;
    }

    @Override
    public void setKillPoint(int killPoint) {
        this.killPoint = killPoint;
    }

    @Override
    public int getFinalKillPoint() {
        return finalKillPoint;
    }

    @Override
    public void setFinalKillPoint(int finalKillPoint) {
        this.finalKillPoint = finalKillPoint;
    }

    @Override
    public int getDeathPoint() {
        return deathPoint;
    }

    @Override
    public void setDeathPoint(int deathPoint) {
        this.deathPoint = deathPoint;
    }

    @Override
    public Duration getTrapTriggerDelay() {
        return trapTriggerDelay;
    }

    @Override
    public void setTrapTriggerDelay(Duration trapTriggerDelay) {
        this.trapTriggerDelay = trapTriggerDelay;
    }

    @Override
    public boolean isTrapQueueEnabled() {
        return trapQueueEnabled;
    }

    @Override
    public void setTrapQueueEnabled(boolean trapQueueEnabled) {
        this.trapQueueEnabled = trapQueueEnabled;
    }

    @Override
    public int getTrapQueueSize() {
        return trapQueueSize;
    }

    @Override
    public void setTrapQueueSize(int trapQueueSize) {
        this.trapQueueSize = trapQueueSize;
    }

    @Override
    public ArenaSettingsImpl clone() {
        return new ArenaSettingsImpl(this.toProperties());
    }

    @Override
    public NamedProperties toProperties() {
        return NamedProperties.builder()
                .add("startTimer", startTimer)
                .add("respawnTime", respawnTime)
                .add("islandRadius", islandRadius)
                .add("minYAxis", minYAxis)
                .add("playerHitTagLength", playerHitTagLength)
                .add("gameEndDelay", gameEndDelay)
                .add("disableHunger", disableHunger)
                .add("bedDestroyPoint", bedDestroyPoint)
                .add("killPoint", killPoint)
                .add("finalKillPoint", finalKillPoint)
                .add("deathPoint", deathPoint)
                .add("trapTriggerDelay", trapTriggerDelay)
                .add("trapQueueEnabled", trapQueueEnabled)
                .add("trapQueueSize", trapQueueSize)
                .build();
    }

}
