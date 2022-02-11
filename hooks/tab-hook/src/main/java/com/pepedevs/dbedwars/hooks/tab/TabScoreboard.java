package com.pepedevs.dbedwars.hooks.tab;

import com.pepedevs.dbedwars.api.adventure.AdventureUtils;
import com.pepedevs.dbedwars.api.hooks.scoreboard.Scoreboard;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.messaging.message.LegacyMessage;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.util.Key;
import me.neznamy.tab.api.scoreboard.Line;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TabScoreboard implements Scoreboard {

    private final TabHook tabHook;
    private final TabScoreboardHook scoreboardHook;
    private final me.neznamy.tab.shared.features.scoreboard.ScoreboardImpl tabScoreboard;

    public TabScoreboard(TabHook tabHook, TabScoreboardHook scoreboardHook, me.neznamy.tab.shared.features.scoreboard.ScoreboardImpl scoreboard) {
        this.tabHook = tabHook;
        this.scoreboardHook = scoreboardHook;
        this.tabScoreboard = scoreboard;
    }

    @Override
    public void setTitle(Message title) {
        this.tabScoreboard.setTitle(AdventureUtils.toVanillaString(title.asComponent()[0]));
    }

    @Override
    public Message getTitle() {
        return AdventureMessage.from(this.tabScoreboard.getTitle());
    }

    @Override
    public Message getLine(int index) {
        return LegacyMessage.from(this.tabScoreboard.getLines().get(index).getText());
    }

    @Override
    public void addLines(Collection<Message> lines) {
        for (Message line : lines) {
            this.tabScoreboard.addLine(AdventureUtils.toVanillaString(line.asComponent()[0]));
        }
    }

    @Override
    public void setLine(int index, Message line) {
        this.tabScoreboard.getLines().get(index).setText(AdventureUtils.toVanillaString(line.asComponent()[0]));
    }

    @Override
    public List<Message> getLines() {
        List<Message> messages = new ArrayList<>();
        for (Line line : this.tabScoreboard.getLines()) {
            messages.add(LegacyMessage.from(line.getText()));
        }
        return Collections.unmodifiableList(messages);
    }

    @Override
    public void clearLines() {
        for (int i = 0; i < this.tabScoreboard.getLines().size(); i++) {
            this.tabScoreboard.removeLine(i);
        }
    }

    @Override
    public void removeLine(int index) {
        this.tabScoreboard.removeLine(index);
    }

    @Override
    public Player getViewer() {
        try {
            return Bukkit.getPlayer(this.tabScoreboard.getPlayers().iterator().next().getUniqueId());
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public void update() {}

    @Override
    public Key<String> getKey() {
        return Key.of(this.tabScoreboard.getName());
    }
}
