package com.pepedevs.dbedwars.utils;

import com.pepedevs.corelib.scoreboard.SimpleScoreboard;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableScoreboard;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ScoreboardImpl {

    private final DBedwars plugin;
    private final ConfigurableScoreboard cfgScore;

    private SimpleScoreboard scoreboard;

    public ScoreboardImpl(DBedwars plugin, ConfigurableScoreboard cfgScore) {
        this.plugin = plugin;
        this.cfgScore = cfgScore;
    }

    public void createScoreboard() {
        ScoreboardImpl.this.scoreboard =
                new SimpleScoreboard(
                        StringUtils.translateAlternateColorCodes(
                                ScoreboardImpl.this.cfgScore.getTitle()),
                        StringUtils.translateAlternateColorCodes(
                                ScoreboardImpl.this.cfgScore.getContent().toArray(new String[0])));
    }

    public void show(Player... player) {
        this.scoreboard.addViewer(player);
    }

    public void show(Collection<Player> players) {
        this.scoreboard.addViewers(players);
    }

    public void hide(Player... player) {
        this.scoreboard.removeViewer(player);
    }

    public void hide(Collection<Player> players) {
        this.scoreboard.removeViewers(players);
    }

    public SimpleScoreboard getHandle() {
        return this.scoreboard;
    }
}
