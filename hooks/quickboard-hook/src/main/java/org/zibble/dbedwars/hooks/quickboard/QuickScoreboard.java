package org.zibble.dbedwars.hooks.quickboard;

import me.tade.quickboard.PlayerBoard;
import me.tade.quickboard.api.QuickBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.api.hooks.scoreboard.Scoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Key;

import java.util.*;

public class QuickScoreboard implements Scoreboard {

    protected final UUID player;
    protected Message title;
    protected final List<Message> lines;
    protected PlayerBoard board;

    public QuickScoreboard(Message title, List<Message> lines, Player player) {
        this.player = player.getUniqueId();
        this.title = title;
        this.lines = lines;
    }

    public void init() {
        List<String> scoreboardLines = new ArrayList<>();
        for (Message line : lines) {
            scoreboardLines.add(AdventureUtils.toVanillaString(line.asComponentWithPAPI(this.getViewer())[0]));
        }
        this.board = QuickBoardAPI.createBoard(this.getViewer(), scoreboardLines,
                Collections.singletonList(AdventureUtils.toVanillaString(title.asComponentWithPAPI(this.getViewer())[0])), 5, 5);
    }

    @Override
    public void setTitle(Message title) {
        this.title = title;
        if (!this.isShown()) return;
        this.board.getTitle().clear();
        this.board.getTitle().add(AdventureUtils.toVanillaString(title.asComponentWithPAPI(this.board.getPlayer())[0]));
    }

    @Override
    public Message getTitle() {
        return this.title;
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
            this.board.getList().add(AdventureUtils.toVanillaString(line.asComponentWithPAPI(this.board.getPlayer())[0]));
        }
    }

    @Override
    public void setLine(int index, Message line) {
        this.lines.set(index, line);
        if (!this.isShown()) return;
        this.board.getList().set(index, AdventureUtils.toVanillaString(line.asComponentWithPAPI(this.board.getPlayer())[0]));
    }

    @Override
    public List<Message> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    @Override
    public void clearLines() {
        this.lines.clear();
        if (!this.isShown()) return;
        this.board.getList().clear();
    }

    @Override
    public void removeLine(int index) {
        this.lines.remove(index);
        if (!this.isShown()) return;
        this.board.getList().remove(index);
    }

    @Override
    public Player getViewer() {
        return Bukkit.getPlayer(this.player);
    }

    @Override
    public void update() {
        if (!this.isShown()) return;
        this.board.updateText();
        this.board.updateTitle();
    }

    @Override
    public void show() {
        this.init();
    }

    @Override
    public void hide() {
        this.board = null;
        QuickBoardAPI.removeBoard(this.getViewer());
    }

    @Override
    public boolean isShown() {
        return this.board != null && this.getViewer() != null;
    }


    @Override
    public Key<String> getKey() {
        return Key.of(this.board.toString());
    }
}
