package com.pepedevs.dbedwars.messaging.parser;

import com.pepedevs.corelib.placeholders.PlaceholderUtil;
import com.pepedevs.dbedwars.api.messaging.MessageParser;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class MiniMessageParser implements MessageParser {

    private MiniMessage instance;

    public MiniMessageParser(MiniMessage instance) {
        this.instance = instance;
    }

    public String parse(String message) {
        return message;
    }

    public String parseWithPAPI(String message, Player player) {
        String parsedMessage = PlaceholderUtil.getManager().apply(player, message);
        return this.parse(parsedMessage);
    }

    public String parseWithPlaceholder(
            String message, Player player, String placeholder, String replacement) {
        String parsedMessage = this.replacer(message, placeholder, replacement);
        return this.parseWithPAPI(parsedMessage, player);
    }

    public String parseWithPlaceholder(
            String message, Player player, PlaceholderEntry... placeholders) {
        String parsedMessage = message;
        for (PlaceholderEntry entry : placeholders) {
            parsedMessage =
                    parsedMessage.replace(entry.getPlaceholder(), entry.getReplacement().get());
        }
        parsedMessage = PlaceholderUtil.getManager().apply(player, parsedMessage);
        return this.parse(parsedMessage);
    }

    public String parseWithPlaceholder(
            String message, Player player, Set<PlaceholderEntry> placeholders) {
        String parsedMessage = message;
        for (PlaceholderEntry entry : placeholders) {
            parsedMessage = parsedMessage.replace(entry.getPlaceholder(), entry.getReplacement().get());
        }
        parsedMessage = PlaceholderUtil.getManager().apply(player, parsedMessage);
        return this.parse(parsedMessage);
    }

    @Override
    public String parseFakePlaceholder(String message, String placeholder, String replacement) {
        String parsedMessage = this.replacer(message, placeholder, replacement);
        return this.parse(parsedMessage);
    }

    @Override
    public String parseFakePlaceholder(String message, PlaceholderEntry... placeholders) {
        String parsedMessage = message;
        for (PlaceholderEntry entry : placeholders) {
            parsedMessage = parsedMessage.replace(entry.getPlaceholder(), entry.getReplacement().get());
        }
        return this.parse(parsedMessage);
    }

    @Override
    public String parseFakePlaceholder(String message, Set<PlaceholderEntry> placeholders) {
        String parsedMessage = message;
        for (PlaceholderEntry entry : placeholders) {
            parsedMessage =
                    parsedMessage.replace(entry.getPlaceholder(), entry.getReplacement().get());
        }
        return this.parse(parsedMessage);
    }

    private String replacer(String message, String placeholder, String replacement) {
        return message.replace(placeholder, replacement);
    }

    public void setInstance(MiniMessage instance) {
        this.instance = instance;
    }
}
