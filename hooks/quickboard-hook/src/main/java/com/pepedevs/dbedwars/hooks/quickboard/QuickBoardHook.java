package com.pepedevs.dbedwars.hooks.quickboard;

import com.pepedevs.dbedwars.api.adventure.AdventureUtils;
import com.pepedevs.dbedwars.api.hooks.scoreboard.Scoreboard;
import com.pepedevs.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import com.pepedevs.dbedwars.api.messaging.Messaging;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.plugin.PluginDependence;
import me.tade.quickboard.PlayerBoard;
import me.tade.quickboard.api.QuickBoardAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuickBoardHook extends PluginDependence implements ScoreboardHook {

    public QuickBoardHook() {
        super("QuickBoard");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into QuickBoard!"));
        }
        return true;
    }

    @Override
    public void disable() {
    }


    @Override
    public Scoreboard getCurrentScoreboard(Player player) {
        for (PlayerBoard board : QuickBoardAPI.getBoards()) {
            if (board.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return new QuickScoreboard(board);
            }
        }
        return null;
    }

    @Override
    public Scoreboard createStaticScoreboard(Player player, Message title, List<Message> lines) {
        List<String> scoreboardLines = new ArrayList<>();
        for (Message line : lines) {
            scoreboardLines.add(AdventureUtils.toVanillaString(line.asComponentWithPAPI(player)[0]));
        }
        PlayerBoard board = QuickBoardAPI.createBoard(player, scoreboardLines,
                Collections.singletonList(AdventureUtils.toVanillaString(title.asComponentWithPAPI(player)[0])), 5, 5);
        return new QuickScoreboard(board);
    }

    @Override
    public Scoreboard createDynamicScoreboard(Player player, Message title, List<Message> lines) {
        return null;
    }

}
