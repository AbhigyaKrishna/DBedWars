package com.pepedevs.dbedwars.utils;

import com.pepedevs.corelib.scoreboard.Scoreboard;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableScoreboard;

public class ScoreboardWrapper extends Scoreboard {

    public static ScoreboardWrapper from(ConfigurableScoreboard cfgScore) {
        ScoreboardWrapper scoreboard = new ScoreboardWrapper(StringUtils.translateAlternateColorCodes(cfgScore.getTitle()));
        scoreboard.addLines(StringUtils.translateAlternateColorCodes(cfgScore.getContent()));
        return scoreboard;
    }

    public ScoreboardWrapper(String title) {
        super(title);
    }

}
