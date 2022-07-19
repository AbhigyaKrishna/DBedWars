package org.zibble.dbedwars.hooks.tab.scoreboard;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.scoreboard.ScoreboardManager;
import me.neznamy.tab.shared.features.scoreboard.ScoreboardImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.api.hooks.scoreboard.Scoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.*;

public class TabScoreboard implements Scoreboard {

    protected final ScoreboardManager manager;
    protected final UUID playerID;
    protected ScoreboardImpl tabScoreboard;
    protected Message title;
    protected List<Message> lines;

    public TabScoreboard(UUID playerID, ScoreboardManager manager, Message title, List<Message> lines) {
        this.playerID = playerID;
        this.manager = manager;
        this.title = title;
        this.lines = lines;
    }

    public void init() {
        Player player = this.getViewer();
        List<String> scoreboardLines = new ArrayList<>();
        for (Message line : this.lines) {
            scoreboardLines.add(AdventureUtils.toVanillaString(line.asComponentWithPAPI(player)[0]));
        }

        this.tabScoreboard = (ScoreboardImpl) manager.createScoreboard(this.playerID.toString(), AdventureUtils.toVanillaString(this.title.asComponentWithPAPI(player)[0]), scoreboardLines);
    }

    @Override
    public Message getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(Message title) {
        this.title = title;
        if (!this.isShown()) return;
        this.tabScoreboard.setTitle(AdventureUtils.toVanillaString(title.asComponentWithPAPI(this.getViewer())[0]));
    }

    @Override
    public Message getLine(int index) {
        return this.lines.get(index);
    }

    @Override
    public void addLines(Collection<Message> lines) {
        this.lines.addAll(lines);
        if (!this.isShown()) return;
        for (Message line : lines) {
            this.tabScoreboard.addLine(AdventureUtils.toVanillaString(line.asComponentWithPAPI(this.getViewer())[0]));
        }
    }

    @Override
    public void setLine(int index, Message line) {
        this.lines.set(index, line);
        if (!this.isShown()) return;
        this.tabScoreboard.getLines().get(index).setText(AdventureUtils.toVanillaString(line.asComponentWithPAPI(this.getViewer())[0]));
    }

    @Override
    public List<Message> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    @Override
    public void clearLines() {
        this.lines.clear();
        if (!this.isShown()) return;
        for (int i = 0; i < this.tabScoreboard.getLines().size(); i++) {
            this.tabScoreboard.removeLine(i);
        }
    }

    @Override
    public void removeLine(int index) {
        this.lines.remove(index);
        if (!this.isShown()) return;
        this.tabScoreboard.removeLine(index);
    }

    @Override
    public Player getViewer() {
        return Bukkit.getPlayer(this.playerID);
    }

    @Override
    public void update() {
        this.tabScoreboard.refresh(TabAPI.getInstance().getPlayer(this.playerID), true);
    }

    @Override
    public void show() {
        this.init();
    }

    @Override
    public void hide() {
        this.tabScoreboard = null;
        this.tabScoreboard.unregister();
    }

    @Override
    public boolean isShown() {
        return this.tabScoreboard != null && this.getViewer() != null;
    }

    @Override
    public void refreshLines() {
        this.update();
    }

    @Override
    public Key getKey() {
        return Key.of(this.tabScoreboard.getName());
    }

    public ScoreboardImpl getTabScoreboard() {
        return tabScoreboard;
    }

}
