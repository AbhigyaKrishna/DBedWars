package com.pepedevs.dbedwars.api.hooks.scoreboard;

import com.pepedevs.dbedwars.api.messaging.message.Message;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;

public interface Scoreboard {

    void setTitle(Message title);

    Message getTitle();

    Message getLine(int index);

    default void addLine(Message line) {
        this.addLines(line);
    }

    default void addLines(Message... lines) {
        this.addLines(Arrays.asList(lines));
    }

    void addLines(Collection<Message> lines);

    void setLine(int index, Message line);

    Collection<Message> getLines();

    void clearLines();

    void removeLine(int index);

    Player getViewer();

    void update();

}
