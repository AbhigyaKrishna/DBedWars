package com.pepedevs.dbedwars.hooks.quickboard;

import com.pepedevs.dbedwars.api.adventure.AdventureUtils;
import com.pepedevs.dbedwars.api.hooks.scoreboard.Scoreboard;
import com.pepedevs.dbedwars.api.messaging.message.LegacyMessage;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.util.Key;
import me.tade.quickboard.PlayerBoard;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QuickScoreboard implements Scoreboard {

    private final PlayerBoard board;

    public QuickScoreboard(PlayerBoard board) {
        this.board = board;
    }

    @Override
    public void setTitle(Message title) {
        this.board.getTitle().clear();
        this.board.getTitle().add(AdventureUtils.toVanillaString(title.asComponentWithPAPI(this.board.getPlayer())[0]));
    }

    @Override
    public Message getTitle() {
        return LegacyMessage.from(this.board.getTitle().get(0));
    }

    @Override
    public Message getLine(int index) {
        return LegacyMessage.from(this.board.getList().get(index));
    }

    @Override
    public void addLines(Collection<Message> lines) {
        for (Message line : lines) {
            this.board.getList().add(AdventureUtils.toVanillaString(line.asComponentWithPAPI(this.board.getPlayer())[0]));
        }
    }

    @Override
    public void setLine(int index, Message line) {
        this.board.getList().set(index, AdventureUtils.toVanillaString(line.asComponentWithPAPI(this.board.getPlayer())[0]));
    }

    @Override
    public List<Message> getLines() {
        List<Message> lines = new ArrayList<>();
        for (String line : this.board.getList()) {
            lines.add(LegacyMessage.from(line));
        }
        return Collections.unmodifiableList(lines);
    }

    @Override
    public void clearLines() {
        this.board.getList().clear();
    }

    @Override
    public void removeLine(int index) {
        this.board.getList().remove(index);
    }

    @Override
    public Player getViewer() {
        return this.board.getPlayer();
    }

    @Override
    public void update() {
        this.board.updateText();
        this.board.updateTitle();
    }


    @Override
    public Key<String> getKey() {
        return null;
    }
}
