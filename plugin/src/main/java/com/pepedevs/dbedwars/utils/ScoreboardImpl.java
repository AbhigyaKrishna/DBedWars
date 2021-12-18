package com.pepedevs.dbedwars.utils;

import com.pepedevs.corelib.scoreboard.Scoreboard;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableScoreboard;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ScoreboardImpl {

    private final DBedwars plugin;
    private final ConfigurableScoreboard cfgScore;

    private Scoreboard scoreboard;

    public ScoreboardImpl(DBedwars plugin, ConfigurableScoreboard cfgScore) {
        this.plugin = plugin;
        this.cfgScore = cfgScore;
    }

    public void createScoreboard() {
        ScoreboardImpl.this.scoreboard =
                new Scoreboard(
                        StringUtils.translateAlternateColorCodes(
                                ScoreboardImpl.this.cfgScore.getTitle()),
                        StringUtils.translateAlternateColorCodes(
                                ScoreboardImpl.this.cfgScore.getContent().toArray(new String[0])));
    }

    public void show(Player... players) {
        this.scoreboard.show(players);
    }

    public void show(Collection<Player> players) {
        this.scoreboard.show(players.toArray(new Player[0]));
    }

    public void hide(Player... players) {
        this.scoreboard.hide(players);
    }

    public void hide(Collection<Player> players) {
        this.scoreboard.hide(players.toArray(new Player[0]));
    }

    public Scoreboard getHandle() {
        return this.scoreboard;
    }
}
