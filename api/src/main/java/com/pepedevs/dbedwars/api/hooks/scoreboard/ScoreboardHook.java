package com.pepedevs.dbedwars.api.hooks.scoreboard;

import com.pepedevs.dbedwars.api.messaging.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ScoreboardHook {

    Scoreboard getCurrentScoreboard(Player player);

    Scoreboard createStaticScoreboard(Player player, Message title, List<Message> lines);

    Scoreboard createDynamicScoreboard(Player player, Message title, List<Message> lines);

}
