package org.zibble.dbedwars.api.util;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import java.util.Optional;

public enum Color {

    WHITE((byte) 0,
            org.bukkit.Color.WHITE,
            ChatColor.WHITE,
            NamedTextColor.WHITE,
            "<white>",
            "WHITE",
            "WHITE",
            DyeColor.WHITE),
    ORANGE((byte) 1,
            org.bukkit.Color.ORANGE,
            ChatColor.GOLD,
            NamedTextColor.GOLD,
            "<gold>",
            "ORANGE",
            "ORANGE",
            DyeColor.ORANGE),
    MAGENTA((byte) 2,
            org.bukkit.Color.fromRGB(189, 68, 179),
            ChatColor.DARK_PURPLE,
            NamedTextColor.DARK_PURPLE,
            "<dark_purple>",
            "MAGENTA",
            "MAGENTA",
            DyeColor.MAGENTA),
    AQUA((byte) 3,
            org.bukkit.Color.AQUA,
            ChatColor.AQUA,
            NamedTextColor.AQUA,
            "<aqua>",
            "LIGHT_BLUE",
            "AQUA",
            DyeColor.LIGHT_BLUE),
    YELLOW((byte) 4,
            org.bukkit.Color.YELLOW,
            ChatColor.YELLOW,
            NamedTextColor.YELLOW,
            "<yellow>",
            "YELLOW",
            "YELLOW",
            DyeColor.YELLOW),
    LIME((byte) 5,
            org.bukkit.Color.LIME,
            ChatColor.GREEN,
            NamedTextColor.GREEN,
            "<green>",
            "LIME",
            "LIME",
            DyeColor.LIME),
    PINK((byte) 6,
            org.bukkit.Color.fromRGB(237, 141, 172),
            ChatColor.LIGHT_PURPLE,
            NamedTextColor.LIGHT_PURPLE,
            "<light_purple>",
            "PINK",
            "PINK",
            DyeColor.PINK),
    GRAY((byte) 7,
            org.bukkit.Color.GRAY,
            ChatColor.DARK_GRAY,
            NamedTextColor.DARK_GRAY,
            "<dark_gray>",
            "GRAY",
            "GRAY",
            DyeColor.GRAY),
    LIGHT_GRAY((byte) 8,
            org.bukkit.Color.fromRGB(142, 142, 134),
            ChatColor.GRAY,
            NamedTextColor.GRAY,
            "<gray>",
            "LIGHT_GRAY",
            "LIGHT_GRAY",
            DyeColor.LIGHT_GRAY),
    CYAN((byte) 9,
            org.bukkit.Color.fromRGB(21, 137, 145),
            ChatColor.DARK_AQUA,
            NamedTextColor.DARK_AQUA,
            "<dark_aqua>",
            "CYAN",
            "CYAN",
            DyeColor.CYAN),
    PURPLE((byte) 10,
            org.bukkit.Color.fromRGB(121, 42, 172),
            ChatColor.BLUE,
            NamedTextColor.BLUE,
            "<blue>",
            "PURPLE",
            "PURPLE",
            DyeColor.PURPLE),
    BLUE((byte) 11,
            org.bukkit.Color.BLUE,
            ChatColor.DARK_BLUE,
            NamedTextColor.DARK_BLUE,
            "<dark_blue>",
            "BLUE",
            "BLUE",
            DyeColor.BLUE),
    BROWN((byte) 12,
            org.bukkit.Color.fromRGB(114, 71, 40),
            ChatColor.DARK_RED,
            NamedTextColor.DARK_RED,
            "<dark_red>",
            "BROWN",
            "BROWN",
            DyeColor.BROWN),
    GREEN((byte) 13,
            org.bukkit.Color.GREEN,
            ChatColor.DARK_GREEN,
            NamedTextColor.DARK_GREEN,
            "<dark_green>",
            "GREEN",
            "GREEN",
            DyeColor.GREEN),
    RED((byte) 14,
            org.bukkit.Color.RED,
            ChatColor.RED,
            NamedTextColor.RED,
            "<red>",
            "RED",
            "RED",
            DyeColor.RED),
    BLACK((byte) 15,
            org.bukkit.Color.BLACK,
            ChatColor.BLACK,
            NamedTextColor.BLACK,
            "<black>",
            "BLACK",
            "BLACK",
            DyeColor.BLACK);

    public static final Color[] VALUES = values();

    private final byte data;
    private final org.bukkit.Color color;
    private final ChatColor chatColor;
    private final NamedTextColor colorComponent;
    private final String miniCode;
    private final String prefix;
    private final String name;
    private final DyeColor dyeColor;
    private final java.awt.Color javaColor;

    Color(byte data,
          org.bukkit.Color color,
          ChatColor chatColor,
          NamedTextColor colorComponent,
          String miniCode,
          String prefix,
          String name,
          DyeColor dyeColor) {
        this.data = data;
        this.color = color;
        this.chatColor = chatColor;
        this.colorComponent = colorComponent;
        this.miniCode = miniCode;
        this.prefix = prefix;
        this.name = name; // TODO: change name according to language
        this.dyeColor = dyeColor;
        this.javaColor = new java.awt.Color(color.asRGB());
    }

    public byte getData() {
        return data;
    }

    public org.bukkit.Color getColor() {
        return color;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public NamedTextColor getColorComponent() {
        return colorComponent;
    }

    public String getMiniCode() {
        return miniCode;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public java.awt.Color asJavaColor() {
        return new java.awt.Color(this.javaColor.getRGB());
    }

    public static Optional<Color> from(org.bukkit.Color color) {
        for (Color value : VALUES) {
            if (value.color.equals(color)) return Optional.of(value);
        }
        return Optional.empty();
    }

    public static Optional<Color> from(ChatColor color) {
        for (Color value : VALUES) {
            if (value.chatColor == color) return Optional.of(value);
        }
        return Optional.empty();
    }

    public static Optional<Color> from(NamedTextColor color) {
        for (Color value : VALUES) {
            if (value.colorComponent.equals(color)) return Optional.of(value);
        }
        return Optional.empty();
    }

    public static Optional<Color> from(String miniCode) {
        for (Color value : VALUES) {
            if (value.miniCode.equalsIgnoreCase(miniCode)) return Optional.of(value);
        }
        return Optional.empty();
    }

    public static Optional<Color> from(DyeColor dyeColor) {
        for (Color value : VALUES) {
            if (value.dyeColor == dyeColor) return Optional.of(value);
        }
        return Optional.empty();
    }

    public static Optional<Color> from(byte data) {
        for (Color value : VALUES) {
            if (value.data == data) return Optional.of(value);
        }
        return Optional.empty();
    }

}
