package com.pepedevs.dbedwars.messaging.parser;

import com.pepedevs.dbedwars.api.messaging.MessageParser;
import com.pepedevs.corelib.placeholders.PlaceholderUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.entity.Player;

public class MiniMessageParser implements MessageParser {

    private MiniMessage instance;

    public MiniMessageParser(MiniMessage instance) {
        this.instance = instance;
    }

    public Component parse(String message) {
        return instance.parse(message);
    }

    public Component parseWithPAPI(String message, Player player) {
        String parsedMessage = PlaceholderUtil.getManager().apply(player, message);
        return this.parse(parsedMessage);
    }

    public Component parseWithPlaceholder(
            String message, Player player, String placeholder, String replacement) {
        String parsedMessage = this.replacer(message, placeholder, replacement);
        return this.parseWithPAPI(message, player);
    }

    public Component parseWithPlaceholder(String message, Player player, Template... placeholders) {
        String parsedMessage = PlaceholderUtil.getManager().apply(player, message);
        return instance.parse(parsedMessage, placeholders);
    }

    private String replacer(String message, String placeholder, String replacement) {
        String returnMessage = message;
        while (returnMessage.contains(placeholder)) {
            returnMessage = returnMessage.replace(placeholder, replacement);
        }

        return returnMessage;
    }

    public void setInstance(MiniMessage instance) {
        this.instance = instance;
    }
}
