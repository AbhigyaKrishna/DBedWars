package org.zibble.dbedwars.api.hooks.scoreboard;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.objects.serializable.Duration;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardData implements Cloneable {

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

    public Scoreboard show(Player player, Placeholder... placeholders) {
        Message title = this.title.clone();
        title.addPlaceholders(placeholders);
        List<Message> elements = new ArrayList<>();
        for (Message element : this.elements) {
            Message clone = element.clone();
            clone.addPlaceholders(placeholders);
            elements.add(clone);
        }
        switch (type) {
            case STATIC:
                return this.hook.createStaticScoreboard(player, title, elements);
            case DYNAMIC:
                return this.hook.createDynamicScoreboard(player, title, elements, this.updateInterval == null ? Duration.ofTicks(5) : this.updateInterval);
        }
        return null;
    }

    @Override
    public ScoreboardData clone() {
        return new ScoreboardData(this.hook, this.type, this.title.clone(), this.elements.stream().map(Message::clone).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
    }

    @Override
    public String toString() {
        return "ScoreboardData{" +
                "type=" + type +
                ", title=" + title +
                ", elements=" + elements +
                ", updateInterval=" + updateInterval +
                '}';
    }

    public enum Type {
        STATIC,
        DYNAMIC
    }

}
