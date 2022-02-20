package org.zibble.dbedwars.hooks.quickboard;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.zibble.dbedwars.api.hooks.scoreboard.Scoreboard;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import org.zibble.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.plugin.PluginDependence;
import org.zibble.dbedwars.api.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class QuickBoardHook extends PluginDependence implements ScoreboardHook {

    private Map<UUID, Scoreboard> scoreboards;

    public QuickBoardHook() {
        super("QuickBoard");
    }

    @Override
    public Boolean apply(Plugin plugin) {
        if (plugin != null) {
            Messaging.get().getConsole().sendMessage(AdventureMessage.from("<green>Hooked into QuickBoard!"));
            this.scoreboards = new ConcurrentHashMap<>();
        }
        return true;
    }

    @Override
    public Scoreboard getCurrentScoreboard(Player player) {
        return this.scoreboards.get(player.getUniqueId());
    }

    @Override
    public Scoreboard createStaticScoreboard(Player player, Message title, List<Message> lines) {
        QuickScoreboard scoreboard = new QuickScoreboard(title, lines, player);
        scoreboard.show();
        return this.scoreboards.put(player.getUniqueId(), scoreboard);
    }

    @Override
    public UpdatingScoreboard createDynamicScoreboard(Player player, Message title, List<Message> lines, Duration delay) {
        UpdatingQuickBoard scoreboard = new UpdatingQuickBoard(title, lines, player, delay);
        scoreboard.show();
        scoreboard.startUpdate();
        return (UpdatingScoreboard) this.scoreboards.put(player.getUniqueId(), scoreboard);
    }

    @Override
    public void removeScoreboard(Player player) {
        Scoreboard scoreboard = this.scoreboards.remove(player.getUniqueId());
        if (scoreboard != null) {
            scoreboard.hide();
        }
    }

}
