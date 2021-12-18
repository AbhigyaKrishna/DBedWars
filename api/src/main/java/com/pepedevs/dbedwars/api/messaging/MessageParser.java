package com.pepedevs.dbedwars.api.messaging;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface MessageParser {

    Component parse(String message);

    Component parseWithPAPI(String message, Player player);

    Component parseWithPlaceholder(
            String message, Player player, String placeholder, String replacement);

    Component parseWithPlaceholder(String message, Player player, PlaceholderEntry... placeholders);
}
