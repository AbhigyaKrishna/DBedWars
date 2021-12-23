package com.pepedevs.dbedwars.api.util;

import com.pepedevs.corelib.utils.itemstack.stainedglass.StainedGlassColor;
import com.pepedevs.corelib.utils.itemstack.wool.WoolColor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public enum Color {
    WHITE(
            (byte) 0,
            org.bukkit.Color.WHITE,
            ChatColor.WHITE,
            'f',
            "WHITE",
            "WHITE",
            WoolColor.WHITE,
            StainedGlassColor.WHITE,
            DyeColor.WHITE),
    ORANGE(
            (byte) 1,
            org.bukkit.Color.ORANGE,
            ChatColor.GOLD,
            '6',
            "ORANGE",
            "ORANGE",
            WoolColor.ORANGE,
            StainedGlassColor.ORANGE,
            DyeColor.ORANGE),
    MAGENTA(
            (byte) 2,
            org.bukkit.Color.fromRGB(189, 68, 179),
            ChatColor.DARK_PURPLE,
            '5',
            "MAGENTA",
            "MAGENTA",
            WoolColor.MAGENTA,
            StainedGlassColor.MAGENTA,
            DyeColor.MAGENTA),
    AQUA(
            (byte) 3,
            org.bukkit.Color.AQUA,
            ChatColor.AQUA,
            'b',
            "LIGHT_BLUE",
            "AQUA",
            WoolColor.LIGHT_BLUE,
            StainedGlassColor.LIGHT_BLUE,
            DyeColor.LIGHT_BLUE),
    YELLOW(
            (byte) 4,
            org.bukkit.Color.YELLOW,
            ChatColor.YELLOW,
            'e',
            "YELLOW",
            "YELLOW",
            WoolColor.YELLOW,
            StainedGlassColor.YELLOW,
            DyeColor.YELLOW),
    LIME(
            (byte) 5,
            org.bukkit.Color.LIME,
            ChatColor.GREEN,
            'a',
            "LIME",
            "LIME",
            WoolColor.LIME,
            StainedGlassColor.LIME,
            DyeColor.LIME),
    PINK(
            (byte) 6,
            org.bukkit.Color.fromRGB(237, 141, 172),
            ChatColor.LIGHT_PURPLE,
            'd',
            "PINK",
            "PINK",
            WoolColor.PINK,
            StainedGlassColor.PINK,
            DyeColor.PINK),
    GRAY(
            (byte) 7,
            org.bukkit.Color.GRAY,
            ChatColor.DARK_GRAY,
            '8',
            "GRAY",
            "GRAY",
            WoolColor.GRAY,
            StainedGlassColor.GRAY,
            DyeColor.GRAY),
    LIGHT_GRAY(
            (byte) 8,
            org.bukkit.Color.fromRGB(142, 142, 134),
            ChatColor.GRAY,
            '7',
            "LIGHT_GRAY",
            "LIGHT_GRAY",
            WoolColor.LIGHT_GRAY,
            StainedGlassColor.LIGHT_GRAY,
            DyeColor.SILVER),
    CYAN(
            (byte) 9,
            org.bukkit.Color.fromRGB(21, 137, 145),
            ChatColor.DARK_AQUA,
            '3',
            "CYAN",
            "CYAN",
            WoolColor.CYAN,
            StainedGlassColor.CYAN,
            DyeColor.CYAN),
    PURPLE(
            (byte) 10,
            org.bukkit.Color.fromRGB(121, 42, 172),
            ChatColor.BLUE,
            '9',
            "PURPLE",
            "PURPLE",
            WoolColor.PURPLE,
            StainedGlassColor.PURPLE,
            DyeColor.PURPLE),
    BLUE(
            (byte) 11,
            org.bukkit.Color.BLUE,
            ChatColor.DARK_BLUE,
            '1',
            "BLUE",
            "BLUE",
            WoolColor.BLUE,
            StainedGlassColor.BLUE,
            DyeColor.BLUE),
    BROWN(
            (byte) 12,
            org.bukkit.Color.fromRGB(114, 71, 40),
            ChatColor.DARK_RED,
            '4',
            "BROWN",
            "BROWN",
            WoolColor.BROWN,
            StainedGlassColor.BROWN,
            DyeColor.BROWN),
    GREEN(
            (byte) 13,
            org.bukkit.Color.GREEN,
            ChatColor.DARK_GREEN,
            '2',
            "GREEN",
            "GREEN",
            WoolColor.GREEN,
            StainedGlassColor.GREEN,
            DyeColor.GREEN),
    RED(
            (byte) 14,
            org.bukkit.Color.RED,
            ChatColor.RED,
            'c',
            "RED",
            "RED",
            WoolColor.RED,
            StainedGlassColor.RED,
            DyeColor.RED),
    BLACK(
            (byte) 15,
            org.bukkit.Color.BLACK,
            ChatColor.BLACK,
            '0',
            "BLACK",
            "BLACK",
            WoolColor.BLACK,
            StainedGlassColor.BLACK,
            DyeColor.BLACK);

    private final byte data;
    private final org.bukkit.Color color;
    private final ChatColor chatColor;
    private final char chatSymbol;
    private final String prefix;
    private final String name;
    private final WoolColor woolColor;
    private final StainedGlassColor glassColor;
    private final DyeColor dyeColor;

    Color(
            byte data,
            org.bukkit.Color color,
            ChatColor chatColor,
            char chatSymbol,
            String prefix,
            String name,
            WoolColor woolColor,
            StainedGlassColor glassColor,
            DyeColor dyeColor) {
        this.data = data;
        this.color = color;
        this.chatColor = chatColor;
        this.chatSymbol = chatSymbol;
        this.prefix = prefix;
        this.name = name; // TODO: change name according to language
        this.woolColor = woolColor;
        this.glassColor = glassColor;
        this.dyeColor = dyeColor;
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

    public char getChatSymbol() {
        return chatSymbol;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public WoolColor getWoolColor() {
        return woolColor;
    }

    public StainedGlassColor getGlassColor() {
        return glassColor;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }
}
