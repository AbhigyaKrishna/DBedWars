package org.zibble.dbedwars.hooks.defaults.scoreboard;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.scoreboard.Scoreboard;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import org.zibble.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Duration;

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

    @Override
    public UpdatingScoreboard createDynamicScoreboard(Player player, Message title, List<Message> lines, Duration delay) {
        UpdatingScoreBoardImpl scoreboard = new UpdatingScoreBoardImpl(player, title,delay);
        scoreboard.addLines(lines);
        scoreboard.show();
        this.scoreboards.put(player.getUniqueId(), scoreboard);
        return scoreboard;
    }

    @Override
    public void removeScoreboard(Player player) {
        ScoreboardImpl scoreboard = scoreboards.remove(player.getUniqueId());
        if(scoreboard == null)
            return;

        scoreboard.hide();
    }

}