package com.pepedevs.dbedwars.hooks.tab;

import com.pepedevs.dbedwars.api.adventure.AdventureUtils;
import com.pepedevs.dbedwars.api.hooks.scoreboard.Scoreboard;
import com.pepedevs.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import me.neznamy.tab.api.scoreboard.Line;
import me.neznamy.tab.api.scoreboard.ScoreboardManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabScoreboardHook implements ScoreboardHook {

    private final TabHook hook;
    private final ScoreboardManager manager;

    public TabScoreboardHook(TabHook hook) {
        this.hook = hook;
        this.manager = hook.getApi().getScoreboardManager();
    }

    @Override
    public Scoreboard getCurrentScoreboard(Player player) {
        return null;
    }

    @Override
    public Scoreboard createStaticScoreboard(Player player, Message title, List<Message> lines) {
        List<String> scoreboardLines = new ArrayList<>();
        for (Message line : lines) {
            scoreboardLines.add(AdventureUtils.toVanillaString(line.asComponentWithPAPI(player)[0]));
        }

        manager.createScoreboard(, AdventureUtils.toVanillaString(title.asComponentWithPAPI(player)[0]), scoreboardLines);

    }

}
