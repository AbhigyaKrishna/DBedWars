package org.zibble.dbedwars.api.game;

import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardData;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.BwItemStack;

public interface ArenaCategory {

    String getName();

    Message getDisplayName();

    void setDisplayName(Message displayName);

    Message getDescription();

    void setDescription(Message description);

    BwItemStack getIcon();

    void setIcon(BwItemStack icon);

    ScoreboardData getScoreboardData();

    void setScoreboardData(ScoreboardData scoreboardData);

}
