package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.api.messaging.MessageParser;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class Message implements Cloneable {

    private String message;
    private Set<PlaceholderEntry> placeholders;
    private UUID playerUUID;
    private ParserType parser;

    protected Message() {}

    protected Message(String message) {
        this.message = message;
    }

    public static Message from(String message) {
        Message returnMessage = new Message(message);
        returnMessage.parser = ParserType.CONFIG;
        return returnMessage;
    }

    public static Message empty() {
        return new Message();
    }

    public static Message from(
            String message, Player player, String placeholder, String replacement) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = player.getUniqueId();
        returnMessage.placeholders.add(PlaceholderEntry.of(placeholder, new Supplier<String>() {
            @Override
            public String get() {
                return replacement;
            }
        }));
        returnMessage.parser = ParserType.CONFIG;
        return returnMessage;
    }

    public static Message from(String message, Player player, PlaceholderEntry... placeholders) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = player.getUniqueId();
        returnMessage.placeholders.addAll(Arrays.asList(placeholders));
        returnMessage.parser = ParserType.CONFIG;
        return returnMessage;
    }

    public static Message from(String message, String placeholder, String replacement) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = null;
        returnMessage.placeholders.add(PlaceholderEntry.of(placeholder, new Supplier<String>() {
            @Override
            public String get() {
                return replacement;
            }
        }));
        returnMessage.parser = ParserType.CONFIG;
        return returnMessage;
    }

    public static Message from(String message, PlaceholderEntry... placeholders) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = null;
        returnMessage.placeholders.addAll(Arrays.asList(placeholders));
        returnMessage.parser = ParserType.CONFIG;
        return returnMessage;
    }


    //------------------------------

    public static Message mini(String message) {
        Message returnMessage = new Message(message);
        returnMessage.parser = ParserType.MINI;
        return returnMessage;
    }

    public static Message mini(
            String message, Player player, String placeholder, String replacement) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = player.getUniqueId();
        returnMessage.placeholders.add(PlaceholderEntry.of(placeholder, new Supplier<String>() {
            @Override
            public String get() {
                return replacement;
            }
        }));
        returnMessage.parser = ParserType.MINI;
        return returnMessage;
    }

    public static Message mini(String message, Player player, PlaceholderEntry... placeholders) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = player.getUniqueId();
        returnMessage.placeholders.addAll(Arrays.asList(placeholders));
        returnMessage.parser = ParserType.MINI;
        return returnMessage;
    }

    public static Message mini(String message, String placeholder, String replacement) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = null;
        returnMessage.placeholders.add(PlaceholderEntry.of(placeholder, new Supplier<String>() {
            @Override
            public String get() {
                return replacement;
            }
        }));
        returnMessage.parser = ParserType.MINI;
        return returnMessage;
    }

    public static Message mini(String message, PlaceholderEntry... placeholders) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = null;
        returnMessage.placeholders.addAll(Arrays.asList(placeholders));
        returnMessage.parser = ParserType.MINI;
        return returnMessage;
    }

    //----------------------

    public static Message classic(String message) {
        Message returnMessage = new Message(message);
        returnMessage.parser = ParserType.CLASSIC;
        return returnMessage;
    }

    public static Message classic(
            String message, Player player, String placeholder, String replacement) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = player.getUniqueId();
        returnMessage.placeholders.add(PlaceholderEntry.of(placeholder, new Supplier<String>() {
            @Override
            public String get() {
                return replacement;
            }
        }));
        returnMessage.parser = ParserType.CLASSIC;
        return returnMessage;
    }

    public static Message classic(String message, Player player, PlaceholderEntry... placeholders) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = player.getUniqueId();
        returnMessage.placeholders.addAll(Arrays.asList(placeholders));
        returnMessage.parser = ParserType.CLASSIC;
        return returnMessage;
    }

    public static Message classic(String message, String placeholder, String replacement) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = null;
        returnMessage.placeholders.add(PlaceholderEntry.of(placeholder, new Supplier<String>() {
            @Override
            public String get() {
                return replacement;
            }
        }));
        returnMessage.parser = ParserType.CLASSIC;
        return returnMessage;
    }

    public static Message classic(String message, PlaceholderEntry... placeholders) {
        Message returnMessage = new Message(message);
        returnMessage.playerUUID = null;
        returnMessage.placeholders.addAll(Arrays.asList(placeholders));
        returnMessage.parser = ParserType.CLASSIC;
        return returnMessage;
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
        if (playerUUID != null && Bukkit.getPlayer(playerUUID) != null) {
            return Component.text(parser.getParser().parseWithPlaceholder(message, Bukkit.getPlayer(playerUUID), placeholders));
        }
        return Component.text(parser.getParser().parseFakePlaceholder(message, placeholders));
    }

    public void append(String text) {
        this.message = this.message.concat(text);
    }

    @Override
    public Message clone() {
        return new Message(new String(message));
    }

    private enum ParserType{
        CLASSIC,
        MINI,
        CONFIG;

        public MessageParser getParser() {
            switch (this) {
                case MINI:
                    return MessagingServer.connect().getMiniParser();
                case CLASSIC:
                    return MessagingServer.connect().getClassicParser();
                default:
                    return MessagingServer.connect().getParser();
            }
        }
    }
}
