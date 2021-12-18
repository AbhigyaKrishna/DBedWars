package com.pepedevs.dbedwars.messaging.parser;

import com.pepedevs.corelib.placeholders.PlaceholderUtil;
import com.pepedevs.dbedwars.api.messaging.MessageParser;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ClassicParser implements MessageParser {

    @Override
    public Component parse(String message) {
        return Component.text(message);
    }

    @Override
    public Component parseWithPAPI(String message, Player player) {
        String parsedMessage = PlaceholderUtil.getManager().apply(player, message);
        return this.parse(parsedMessage);
    }

    @Override
    public Component parseWithPlaceholder(
            String message, Player player, String placeholder, String replacement) {
        String parsedMessage = this.replacer(message, placeholder, replacement);
        return this.parseWithPAPI(parsedMessage, player);
    }

    @Override
    public Component parseWithPlaceholder(String message, Player player, PlaceholderEntry... placeholders) {
        String parsedMessage = message;
        for (PlaceholderEntry entry : placeholders) {
            parsedMessage = parsedMessage.replace(entry.getPlaceholder(), entry.getReplacement());
        }
        parsedMessage = PlaceholderUtil.getManager().apply(player, parsedMessage);
        return this.parse(parsedMessage);
    }

    private String replacer(String message, String placeholder, String replacement) {
        return message.replace(placeholder, replacement);
    }
}
