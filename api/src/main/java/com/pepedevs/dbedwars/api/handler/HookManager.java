package com.pepedevs.dbedwars.api.handler;

import com.pepedevs.dbedwars.api.hooks.scoreboard.ScoreboardHook;

public interface HookManager {

    ScoreboardHook getScoreboardHook();

    void setScoreboardHook(ScoreboardHook scoreboardHook);

}
