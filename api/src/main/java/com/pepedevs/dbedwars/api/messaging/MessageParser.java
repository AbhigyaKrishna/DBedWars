package com.pepedevs.dbedwars.api.messaging;

import org.bukkit.entity.Player;

import java.util.Set;

public interface MessageParser {

    String parse(String message);

    String parseWithPAPI(String message, Player player);

    String parseWithPlaceholder(
            String message, Player player, String placeholder, String replacement);

    String parseWithPlaceholder(String message, Player player, PlaceholderEntry... placeholders);

    String parseFakePlaceholder(String message, String placeholder, String replacement);

    String parseFakePlaceholder(String message, PlaceholderEntry... placeholders);

    public String parseFakePlaceholder(String message, Set<PlaceholderEntry> placeholders);

    String parseWithPlaceholder(String message, Player player, Set<PlaceholderEntry> placeholders);

}
