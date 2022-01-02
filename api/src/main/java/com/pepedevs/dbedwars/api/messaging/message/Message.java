package com.pepedevs.dbedwars.api.messaging.message;

import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Message {

    protected int size;
    protected String[] message;
    protected List<PlaceholderEntry> placeholders;

    protected Message(String message, PlaceholderEntry... placeholders) {
        this(new String[]{message}, placeholders);
    }

    protected Message(String[] message, PlaceholderEntry... placeholders) {
        this.message = message;
        this.size = message.length;
        this.placeholders = new ArrayList<>();
        this.placeholders.addAll(Arrays.asList(placeholders));
    }

    public String getMessage() {
        String message = "";
        for (int i = 0; i < this.message.length - 1; i++) {
            message += this.message[i] + "\n";
        }
        message += this.message[this.message.length - 1];
        return message;
    }

    public int size() {
        return this.size;
    }

    public String[] getRawMessage() {
        return this.message;
    }

    public void setMessage(String... message) {
        this.message = message;
        this.size = message.length;
    }

    public Message addLine(String line) {
        if (this.size == this.message.length) {
            this.message = Arrays.copyOf(this.message, this.message.length + 8);
        }

        this.message[this.size] = line;
        this.size++;
        return this;
    }

    public Message removeLine(int index) {
        if (index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException("Index " + index + " is out bound for the message!");

        if (this.size == 1)
            throw new IllegalStateException("Message should have at least one line!");

        if (this.size - 1 - index >= 0)
            System.arraycopy(this.message, index + 1, this.message, index, this.size - 1 - index);

        this.size--;
        return this;
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
