package com.pepedevs.dbedwars.utils;

import com.pepedevs.corelib.scoreboard.Scoreboard;
import com.pepedevs.dbedwars.configuration.Lang;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableScoreboard;
import net.kyori.adventure.text.Component;

public class ScoreboardWrapper extends Scoreboard {

    public static ScoreboardWrapper from(ConfigurableScoreboard cfgScore) {
        ScoreboardWrapper scoreboard = new ScoreboardWrapper(Lang.getTranslator().translate(cfgScore.getTitle()));
        scoreboard.addLines(Lang.getTranslator().translate(cfgScore.getContent()));
        return scoreboard;
    }

    public ScoreboardWrapper(String title) {
        super(title);
    }

    public ScoreboardWrapper(Component title) {
        super(title);
    }

}
