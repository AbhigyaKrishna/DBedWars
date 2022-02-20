package org.zibble.dbedwars.api.handler;

import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;

public interface HookManager {

    ScoreboardHook getScoreboardHook();

    void setScoreboardHook(ScoreboardHook scoreboardHook);

}
