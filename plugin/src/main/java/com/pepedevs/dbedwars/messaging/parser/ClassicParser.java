package com.pepedevs.dbedwars.messaging.parser;

import com.pepedevs.dbedwars.api.messaging.MessageParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.entity.Player;

public class ClassicParser implements MessageParser {

    @Override
    public Component parse(String message) {
        return Component.text(message);
    }

    @Override
    public Component parseWithPAPI(String message, Player player) {
        return null;
    }

    @Override
    public Component parseWithPlaceholder(
            String message, Player player, String placeholder, String replacement) {
        return null;
    }

    @Override
    public Component parseWithPlaceholder(String message, Player player, Template... placeholders) {
        return null;
    }

    @Override
    public void setPlaceholderReplacementCount(int replacementCount) {}
}
