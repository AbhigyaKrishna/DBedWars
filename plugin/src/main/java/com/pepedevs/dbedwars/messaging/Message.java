package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Message implements Cloneable {

    private Component component;

    protected Message(Component component) {
        this.component = component;
    }

    protected Message() {}

    public static Message from(String message) {
        return new Message(MessagingServer.connect().getParser().parse(message));
    }

    public static Message from(Component component) {
        return new Message(component);
    }

    public static Message empty() {
        return new Message();
    }

    public static Message parsed(
            String message, Player player, String placeholder, String replacement) {
        return new Message(
                MessagingServer.connect()
                        .getParser()
                        .parseWithPlaceholder(message, player, placeholder, replacement));
    }

    public static Message parsed(String message, Player player, PlaceholderEntry... placeholders) {
        return new Message(
                MessagingServer.connect()
                        .getParser()
                        .parseWithPlaceholder(message, player, placeholders));
    }

    public static Message parsed(String message, String placeholder, String replacement) {
        return new Message(MessagingServer.connect().getParser().parseFakePlaceholder(message, placeholder, replacement));
    }

    public static Message parsed(String message, PlaceholderEntry... placeholders) {
        return new Message(MessagingServer.connect().getParser().parseFakePlaceholder(message, placeholders));
    }

    public static Message mini(String message) {
        return new Message(MessagingServer.connect().getMiniParser().parse(message));
    }

    public static Message miniParsed(
            String message, Player player, String placeholder, String replacement) {
        return new Message(
                MessagingServer.connect()
                        .getMiniParser()
                        .parseWithPlaceholder(message, player, placeholder, replacement));
    }

    public static Message miniParsed(String message, Player player, PlaceholderEntry... placeholders) {
        return new Message(
                MessagingServer.connect()
                        .getMiniParser()
                        .parseWithPlaceholder(message, player, placeholders));
    }

    public static Message miniParsed(String message, String placeholder, String replacement) {
        return new Message(MessagingServer.connect().getMiniParser().parseFakePlaceholder(message, placeholder, replacement));
    }

    public static Message miniParsed(String message, PlaceholderEntry... placeholders) {
        return new Message(MessagingServer.connect().getClassicParser().parseFakePlaceholder(message, placeholders));
    }

    public static Message classic(String message) {
        return new Message(MessagingServer.connect().getClassicParser().parse(message));
    }

    public static Message classicParsed(
            String message, Player player, String placeholder, String replacement) {
        return new Message(
                MessagingServer.connect()
                        .getClassicParser()
                        .parseWithPlaceholder(message, player, placeholder, replacement));
    }

    public static Message classicParsed(String message, Player player, PlaceholderEntry... placeholders) {
        return new Message(
                MessagingServer.connect()
                        .getClassicParser()
                        .parseWithPlaceholder(message, player, placeholders));
    }

    public static Message classicParsed(String message, String placeholder, String replacement) {
        return new Message(MessagingServer.connect().getClassicParser().parseFakePlaceholder(message, placeholder, replacement));
    }

    public static Message classicParsed(String message, PlaceholderEntry... placeholders) {
        return new Message(MessagingServer.connect().getClassicParser().parseFakePlaceholder(message, placeholders));
    }

    public SentMessage send(MessagingMember sender, MessagingChannel channel) {
        return MessagingServer.connect().sendMessage(this, sender, channel);
    }

    public SentMessage sendToExcept(
            MessagingMember sender, MessagingChannel channel, MessagingMember... hiddenUsers) {
        return MessagingServer.connect().sendToExcept(this, sender, channel, hiddenUsers);
    }

    public SentMessage sendAsServer(MessagingChannel channel) {
        return MessagingServer.connect().sendMessage(this, MessagingMember.ofConsole(), channel);
    }

    public SentMessage logToConsole() {
        return MessagingServer.connect().sendToConsole(this);
    }

    public Component asComponent() {
        return this.component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public void append(String text) {
        this.component = this.component.append(MessagingServer.connect().getParser().parse(text));
    }

    public void append(Component component) {
        this.component = this.component.append(component);
    }

    @Override
    public Message clone() {
        return new Message(asComponent());
    }
}
