package org.zibble.dbedwars.api.hooks.scoreboard;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Duration;

public interface UpdatingScoreboard extends Scoreboard{

    void startUpdate();

    void cancelUpdate();

    Message nextLine(int index);

    Message nextTitle();

    Duration getDelay();

    void setDelay(Duration delay);

}
