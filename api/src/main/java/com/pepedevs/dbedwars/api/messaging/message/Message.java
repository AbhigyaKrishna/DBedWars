package com.pepedevs.dbedwars.api.messaging.message;

import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Message {

    protected List<String> message;
    protected List<PlaceholderEntry> placeholders;

    public static Message empty() {
        return new Message("") {
            @Override
            public Component[] asComponent() {
                return new Component[]{Component.empty()};
            }

            @Override
            public Component[] asComponentWithPAPI(Player player) {
                return new Component[]{Component.empty()};
            }
        };
    }

    protected Message(String message, PlaceholderEntry... placeholders) {
        this.message = Collections.synchronizedList(new ArrayList<>());
        this.message.add(message);
        this.placeholders = new ArrayList<>();
        this.placeholders.addAll(Arrays.asList(placeholders));
    }

    protected Message(Collection<String> message, PlaceholderEntry... placeholders) {
        this.message = Collections.synchronizedList(new ArrayList<>(message));
        this.placeholders = new ArrayList<>();
        this.placeholders.addAll(Arrays.asList(placeholders));
    }

    public String getMessage() {
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < this.message.size() - 1; i++) {
            messageBuilder.append(this.message.get(i)).append("\n");
        }
        messageBuilder.append(this.message.get(this.message.size() - 1));
        return messageBuilder.toString();
    }

    public int size() {
        return this.message.size();
    }

    public List<String> getRawMessage() {
        return this.message;
    }

    public void setMessage(String... message) {
        List<String> newMessage = new ArrayList<>(message.length);
        newMessage.addAll(Arrays.asList(message));
        this.message = newMessage;
    }

    public Message addLine(String line) {
        this.message.add(line);
        return this;
    }

    public Message removeLine(int index) {
        this.message.remove(index);
        return this;
    }

    public List<String> getLines() {
        return new ArrayList<>(this.message);
    }

    public void addPlaceholders(PlaceholderEntry... placeholders) {
        this.placeholders.addAll(Arrays.asList(placeholders));
    }

    public void removePlaceholder(int index) {
        this.placeholders.remove(index);
    }

    public void removePlaceholders(PlaceholderEntry... placeholders) {
        this.placeholders.removeAll(Arrays.asList(placeholders));
    }

    public void clearPlaceholders() {
        this.placeholders.clear();
    }

    public abstract Component[] asComponent();

    public abstract Component[] asComponentWithPAPI(Player player);

}
