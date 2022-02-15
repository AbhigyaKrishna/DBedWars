package com.pepedevs.dbedwars.api.hooks.scoreboard;

import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.util.Duration;

public interface UpdatingScoreboard extends Scoreboard{

    void startUpdate();

    void cancelUpdate();

    Message nextLine(int index);

    Message nextTitle();

    Duration getDelay();

    void setDelay(Duration delay);

}
