package com.pepedevs.dbedwars.messaging.parser;

import com.pepedevs.corelib.placeholders.PlaceholderUtil;
import com.pepedevs.corelib.utils.StringUtils;
import com.pepedevs.dbedwars.api.messaging.MessageParser;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import org.bukkit.entity.Player;

import java.util.Set;

public class LegacyParser implements MessageParser {

    @Override
    public String parse(String message) {
        return StringUtils.translateAlternateColorCodes(message);
    }

    @Override
    public String parseWithPAPI(String message, Player player) {
        String parsedMessage = PlaceholderUtil.getManager().apply(player, message);
        return this.parse(parsedMessage);
    }

    @Override
    public String parseWithPlaceholder(
            String message, Player player, String placeholder, String replacement) {
        String parsedMessage = this.replacer(message, placeholder, replacement);
        return this.parseWithPAPI(parsedMessage, player);
    }

    @Override
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

    @Override
    public String parseWithPlaceholder(
            String message, Player player, Set<PlaceholderEntry> placeholders) {
        String parsedMessage = message;
        for (PlaceholderEntry entry : placeholders) {
            parsedMessage =
                    parsedMessage.replace(entry.getPlaceholder(), entry.getReplacement().get());
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
            parsedMessage =
                    parsedMessage.replace(entry.getPlaceholder(), entry.getReplacement().get());
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
}
