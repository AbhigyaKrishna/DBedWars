package com.pepedevs.dbedwars.api.hooks;

import com.pepedevs.dbedwars.api.messaging.message.Message;
import org.bukkit.entity.Player;

import java.util.List;

public interface ScoreboardHook {

    void createStaticScoreboard(Player player, String id, Message title, List<Message> lines);

}
