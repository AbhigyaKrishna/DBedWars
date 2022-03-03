package org.zibble.dbedwars.api.hooks.scoreboard;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.Hook;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Duration;

import java.util.List;

public interface ScoreboardHook extends Hook {

    Scoreboard getCurrentScoreboard(Player player);

    Scoreboard createStaticScoreboard(Player player, Message title, List<Message> lines);

    UpdatingScoreboard createDynamicScoreboard(Player player, Message title, List<Message> lines, Duration delay);

    void removeScoreboard(Player player);

}
