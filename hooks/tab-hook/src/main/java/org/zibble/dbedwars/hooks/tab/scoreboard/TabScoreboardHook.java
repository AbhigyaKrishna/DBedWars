package org.zibble.dbedwars.hooks.tab.scoreboard;

import me.neznamy.tab.api.scoreboard.ScoreboardManager;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.scoreboard.Scoreboard;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import org.zibble.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.hooks.tab.TabHook;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TabScoreboardHook implements ScoreboardHook {

    private final ScoreboardManager manager;
    private final Map<UUID, Scoreboard> scoreboardMap;

    public TabScoreboardHook(TabHook hook) {
        this.manager = hook.getApi().getScoreboardManager();
        this.scoreboardMap = new ConcurrentHashMap<>();
    }

    @Override
    public Scoreboard getCurrentScoreboard(Player player) {
        return this.scoreboardMap.get(player.getUniqueId());
    }

    @Override
    public Scoreboard createStaticScoreboard(Player player, Message title, List<Message> lines) {
        TabScoreboard scoreboard = new TabScoreboard(player.getUniqueId(), this.manager, title, lines);
        scoreboard.show();
        return this.scoreboardMap.put(player.getUniqueId(), scoreboard);
    }

    @Override
    public UpdatingScoreboard createDynamicScoreboard(Player player, Message title, List<Message> lines, Duration delay) {
        UpdatingTabScoreboard scoreboard = new UpdatingTabScoreboard(player.getUniqueId(), manager, title, lines, delay);
        scoreboard.show();
        scoreboard.startUpdate();
        return (UpdatingScoreboard) this.scoreboardMap.put(player.getUniqueId(), scoreboard);
    }

    @Override
    public void removeScoreboard(Player player) {
        Scoreboard scoreboard = scoreboardMap.remove(player.getUniqueId());
        if (scoreboard != null) {
            scoreboard.hide();
        }
    }

}
