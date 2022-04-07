package org.zibble.dbedwars.api.game.settings;

import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.properies.PropertySerializable;

public interface ArenaSettings extends PropertySerializable, Cloneable {

    int getStartTimer();

    void setStartTimer(int startTimer);

    int getRespawnTime();

    void setRespawnTime(int respawnTime);

    int getIslandRadius();

    void setIslandRadius(int islandRadius);

    int getMinYAxis();

    void setMinYAxis(int minYAxis);

    int getPlayerHitTagLength();

    void setPlayerHitTagLength(int playerHitTagLength);

    int getGameEndDelay();

    void setGameEndDelay(int gameEndDelay);

    boolean isDisableHunger();

    void setDisableHunger(boolean flag);

    int getBedDestroyPoint();

    void setBedDestroyPoint(int bedDestroyPoint);

    int getKillPoint();

    void setKillPoint(int killPoint);

    int getFinalKillPoint();

    void setFinalKillPoint(int finalKillPoint);

    int getDeathPoint();

    void setDeathPoint(int deathPoint);

    Duration getTrapTriggerDelay();

    void setTrapTriggerDelay(Duration trapTriggerDelay);

    boolean isTrapQueueEnabled();

    void setTrapQueueEnabled(boolean trapQueueEnabled);

    int getTrapQueueSize();

    void setTrapQueueSize(int trapQueueSize);

    ArenaSettings clone();

}
