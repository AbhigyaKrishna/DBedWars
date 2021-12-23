package com.pepedevs.dbedwars.api.messaging.message;

import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class Message {

    protected String message;
    protected Set<PlaceholderEntry> placeholders;

    protected Message(String message, PlaceholderEntry... placeholders) {
        this.message = message;
        this.placeholders = new HashSet<>();
        this.placeholders.addAll(Arrays.asList(placeholders));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addPlaceholders(PlaceholderEntry... placeholders) {
        this.placeholders.addAll(Arrays.asList(placeholders));
    }

    public void clearPlaceholders() {
        this.placeholders.clear();
    }

    public abstract Component asComponent();

    public abstract Component asComponentWithPAPI(Player player);

}
