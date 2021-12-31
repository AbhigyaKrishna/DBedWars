package com.pepedevs.dbedwars.utils;

import com.pepedevs.corelib.scoreboard.Scoreboard;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableScoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScoreboardWrapper extends Scoreboard {

    public static ScoreboardWrapper from(ConfigurableScoreboard cfgScore) {
        ScoreboardWrapper scoreboard = new ScoreboardWrapper(StringUtils.translateAlternateColorCodes(cfgScore.getTitle()));
        scoreboard.addLines(StringUtils.translateAlternateColorCodes(cfgScore.getContent()));
        return scoreboard;
    }

    public ScoreboardWrapper(String title) {
        super(title);
    }

    @Override
    public Scoreboard addLines(Collection<String> elements) {
        List<String> lines = new ArrayList<>();
        for (String element : elements) {
            if (element != null && element.length() > 40) {
                lines.add(element.substring(0, 40));
            }
        }
        return super.addLines(lines);
    }

    @Override
    public void setLine(int index, String line) {
        String l = "";
        if (line != null && line.length() > 40) {
            l = line.substring(0, 40);
        }
        super.setLine(index, l);
    }

}
