package org.zibble.dbedwars.api.hooks.scoreboard;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.Duration;

import java.util.List;

public class ScoreboardData {

    private final ScoreboardHook hook;
    private final Type type;
    private Message title;
    private List<Message> elements;
    private Duration updateInterval;

    ScoreboardData(ScoreboardHook hook, Type type, Message title, List<Message> elements) {
        this.hook = hook;
        this.type = type;
        this.title = title;
        this.elements = elements;
    }

    public Type getType() {
        return type;
    }

    public Message getTitle() {
        return title;
    }

    public void setTitle(Message title) {
        this.title = title;
    }

    public List<Message> getElements() {
        return elements;
    }

    public void setElements(List<Message> elements) {
        this.elements = elements;
    }

    public Duration getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Duration updateInterval) {
        this.updateInterval = updateInterval;
    }

    public Scoreboard show(Player player) {
        switch (type) {
            case STATIC:
                return this.hook.createStaticScoreboard(player, this.title, this.elements);
            case DYNAMIC:
                return this.hook.createDynamicScoreboard(player, this.title, this.elements, this.updateInterval == null ? Duration.ofTicks(5) : this.updateInterval);
        }
        return null;
    }

    public enum Type {
        STATIC,
        DYNAMIC
    }
}
