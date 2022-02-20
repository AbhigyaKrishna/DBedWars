package org.zibble.dbedwars.hooks.tab;

import me.neznamy.tab.api.scoreboard.ScoreboardManager;
import me.neznamy.tab.shared.features.scoreboard.ScoreboardImpl;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.api.hooks.scoreboard.Scoreboard;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import org.zibble.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TabScoreboardHook implements ScoreboardHook {

    private final TabHook hook;
    private final ScoreboardManager manager;
    private final ConcurrentHashMap<UUID,TabScoreboard> scoreboardMap;

    public TabScoreboardHook(TabHook hook) {
        this.hook = hook;
        this.manager = hook.getApi().getScoreboardManager();
        this.scoreboardMap = new ConcurrentHashMap<UUID, TabScoreboard>();
    }

    @Override
    public Scoreboard getCurrentScoreboard(Player player) {
        return this.scoreboardMap.get(player.getUniqueId());
    }

    @Override
    public Scoreboard createStaticScoreboard(Player player, Message title, List<Message> lines) {
        List<String> scoreboardLines = new ArrayList<>();
        for (Message line : lines) {
            scoreboardLines.add(AdventureUtils.toVanillaString(line.asComponentWithPAPI(player)[0]));
        }

        final ScoreboardImpl tabScoreboard = (ScoreboardImpl) manager.createScoreboard(player.getUniqueId().toString(), AdventureUtils.toVanillaString(title.asComponentWithPAPI(player)[0]), scoreboardLines);
        final TabScoreboard scoreboard = new TabScoreboard(player.getUniqueId(), this.hook,this, tabScoreboard);
        this.scoreboardMap.put(player.getUniqueId(), scoreboard);
        return scoreboard;
    }

    @Override
    public UpdatingScoreboard createDynamicScoreboard(Player player, Message title, List<Message> lines, Duration delay) {
        List<String> scoreboardLines = new ArrayList<>();
        for (Message line : lines) {
            scoreboardLines.add(AdventureUtils.toVanillaString(line.asComponentWithPAPI(player)[0]));
        }

        final ScoreboardImpl tabScoreboard = (ScoreboardImpl) manager.createScoreboard(player.getUniqueId().toString(), AdventureUtils.toVanillaString(title.asComponentWithPAPI(player)[0]), scoreboardLines);
        final UpdatingTabScoreboard scoreboard = new UpdatingTabScoreboard(player.getUniqueId(), this.hook,this, tabScoreboard,delay);
        this.scoreboardMap.put(player.getUniqueId(), scoreboard);
        return scoreboard;
    }

    @Override
    public void removeScoreboard(Player player) {
        final TabScoreboard tabScoreboard = scoreboardMap.remove(player.getUniqueId());
        if(tabScoreboard == null)
            return;

        tabScoreboard.getTabScoreboard().unregister();
    }

}