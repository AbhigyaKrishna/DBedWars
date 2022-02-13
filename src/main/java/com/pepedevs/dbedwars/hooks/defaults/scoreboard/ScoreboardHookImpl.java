package com.pepedevs.dbedwars.hooks.defaults.scoreboard;

import com.pepedevs.dbedwars.api.hooks.scoreboard.Scoreboard;
import com.pepedevs.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardHookImpl implements ScoreboardHook {

    private final Map<UUID, ScoreboardImpl> scoreboards = new ConcurrentHashMap<>();

    @Override
    public Scoreboard getCurrentScoreboard(Player player) {
        return this.scoreboards.get(player.getUniqueId());
    }

    @Override
    public Scoreboard createStaticScoreboard(Player player, Message title, List<Message> lines) {
        ScoreboardImpl scoreboard = new ScoreboardImpl(player, title);
        scoreboard.addLines(lines);
        scoreboard.show();
        this.scoreboards.put(player.getUniqueId(), scoreboard);
        return scoreboard;
    }

}
