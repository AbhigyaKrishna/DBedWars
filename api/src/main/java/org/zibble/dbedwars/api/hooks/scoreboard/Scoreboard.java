package org.zibble.dbedwars.api.hooks.scoreboard;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Keyed;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface Scoreboard extends Keyed<String> {

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

    List<Message> getLines();

    void clearLines();

    void removeLine(int index);

    Player getViewer();

    void update();

}