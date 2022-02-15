package com.pepedevs.dbedwars.hooks.tab;

import com.pepedevs.dbedwars.api.adventure.AdventureUtils;
import com.pepedevs.dbedwars.api.hooks.scoreboard.Scoreboard;
import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.messaging.message.LegacyMessage;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.util.Key;
import me.neznamy.tab.api.scoreboard.Line;
import me.neznamy.tab.shared.features.scoreboard.ScoreboardImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class TabScoreboard implements Scoreboard {

    private final TabHook tabHook;
    private final TabScoreboardHook scoreboardHook;
    private final me.neznamy.tab.shared.features.scoreboard.ScoreboardImpl tabScoreboard;
    private final UUID playerID;

    public TabScoreboard(UUID playerID,TabHook tabHook, TabScoreboardHook scoreboardHook, me.neznamy.tab.shared.features.scoreboard.ScoreboardImpl scoreboard) {
        this.tabHook = tabHook;
        this.scoreboardHook = scoreboardHook;
        this.tabScoreboard = scoreboard;
        this.playerID = playerID;
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
        return Bukkit.getPlayer(playerID);
    }

    @Override
    public void update() {}

    @Override
    public Key<String> getKey() {
        return Key.of(this.tabScoreboard.getName());
    }

    public ScoreboardImpl getTabScoreboard() {
        return tabScoreboard;
    }
}
