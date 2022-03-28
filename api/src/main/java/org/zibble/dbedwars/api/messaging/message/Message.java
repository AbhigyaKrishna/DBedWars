package org.zibble.dbedwars.api.messaging.message;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;

import java.util.*;

public abstract class Message implements Cloneable {

    protected List<String> message;
    protected transient List<Placeholder> placeholders;

    public static Message empty() {
        return new EmptyMessage();
    }

    protected Message(String message, Placeholder... placeholders) {
        this.message = Collections.synchronizedList(new ArrayList<>());
        this.message.add(message);
        this.placeholders = new ArrayList<>();
        this.placeholders.addAll(Arrays.asList(placeholders));
    }

    protected Message(Collection<String> message, Placeholder... placeholders) {
        this.message = Collections.synchronizedList(new ArrayList<>(message));
        this.placeholders = new ArrayList<>();
        this.placeholders.addAll(Arrays.asList(placeholders));
    }

    public String getMessage() {
        return String.join("\n", this.message);
    }

    public int size() {
        return this.message.size();
    }

    public List<String> getRawMessage() {
        return this.message;
    }

    public void concatLine(int index, String line) {
        this.message.set(index, this.message.get(index) + line);
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

    public Placeholder[] getPlaceholders() {
        return placeholders.toArray(new Placeholder[0]);
    }

    public void addPlaceholders(Placeholder... placeholders) {
        this.addPlaceholders(Arrays.asList(placeholders));
    }

    public void addPlaceholders(Collection<Placeholder> placeholders) {
        this.placeholders.addAll(placeholders);
    }

    public void removePlaceholder(int index) {
        this.placeholders.remove(index);
    }

    public void removePlaceholders(Placeholder... placeholders) {
        this.placeholders.removeAll(Arrays.asList(placeholders));
    }

    public void clearPlaceholders() {
        this.placeholders.clear();
    }

    public abstract Message[] splitToLineMessage();

    public abstract Component[] asRawComponent();

    public abstract Component[] asComponent();

    public abstract Component[] asComponentWithPAPI(Player player);

    @Override
    public abstract Message clone();

    protected static class EmptyMessage extends Message {

        protected EmptyMessage() {
            super("");
        }

        @Override
        public Message[] splitToLineMessage() {
            return new Message[]{new EmptyMessage()};
        }

        @Override
        public Component[] asRawComponent() {
            return this.asComponent();
        }

        @Override
        public Component[] asComponent() {
            return new Component[]{Component.empty()};
        }

        @Override
        public Component[] asComponentWithPAPI(Player player) {
            return new Component[]{Component.empty()};
        }

        @Override
        public Message clone() {
            return new EmptyMessage();
        }

    }

}
