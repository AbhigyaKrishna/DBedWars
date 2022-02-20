package org.zibble.dbedwars.api.hooks.scoreboard;

import org.zibble.dbedwars.api.util.Duration;

public interface UpdatingScoreboard extends Scoreboard{

    void startUpdate();

    void cancelUpdate();

    Duration getDelay();

    void setDelay(Duration delay);

}
