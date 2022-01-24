package com.pepedevs.dbedwars.api.util;

import com.pepedevs.radium.utils.itemstack.stainedglass.StainedGlassColor;
import com.pepedevs.radium.utils.itemstack.wool.WoolColor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public enum Color {

    WHITE((byte) 0,
            org.bukkit.Color.WHITE,
            ChatColor.WHITE,
            NamedTextColor.WHITE,
            "<white>",
            "WHITE",
            "WHITE",
            WoolColor.WHITE,
            StainedGlassColor.WHITE,
            DyeColor.WHITE),
    ORANGE((byte) 1,
            org.bukkit.Color.ORANGE,
            ChatColor.GOLD,
            NamedTextColor.GOLD,
            "<gold>",
            "ORANGE",
            "ORANGE",
            WoolColor.ORANGE,
            StainedGlassColor.ORANGE,
            DyeColor.ORANGE),
    MAGENTA((byte) 2,
            org.bukkit.Color.fromRGB(189, 68, 179),
            ChatColor.DARK_PURPLE,
            NamedTextColor.DARK_PURPLE,
            "<dark_purple>",
            "MAGENTA",
            "MAGENTA",
            WoolColor.MAGENTA,
            StainedGlassColor.MAGENTA,
            DyeColor.MAGENTA),
    AQUA((byte) 3,
            org.bukkit.Color.AQUA,
            ChatColor.AQUA,
            NamedTextColor.AQUA,
            "<aqua>",
            "LIGHT_BLUE",
            "AQUA",
            WoolColor.LIGHT_BLUE,
            StainedGlassColor.LIGHT_BLUE,
            DyeColor.LIGHT_BLUE),
    YELLOW((byte) 4,
            org.bukkit.Color.YELLOW,
            ChatColor.YELLOW,
            NamedTextColor.YELLOW,
            "<yellow>",
            "YELLOW",
            "YELLOW",
            WoolColor.YELLOW,
            StainedGlassColor.YELLOW,
            DyeColor.YELLOW),
    LIME((byte) 5,
            org.bukkit.Color.LIME,
            ChatColor.GREEN,
            NamedTextColor.GREEN,
            "<green>",
            "LIME",
            "LIME",
            WoolColor.LIME,
            StainedGlassColor.LIME,
            DyeColor.LIME),
    PINK((byte) 6,
            org.bukkit.Color.fromRGB(237, 141, 172),
            ChatColor.LIGHT_PURPLE,
            NamedTextColor.LIGHT_PURPLE,
            "<light_purple>",
            "PINK",
            "PINK",
            WoolColor.PINK,
            StainedGlassColor.PINK,
            DyeColor.PINK),
    GRAY((byte) 7,
            org.bukkit.Color.GRAY,
            ChatColor.DARK_GRAY,
            NamedTextColor.DARK_GRAY,
            "<dark_gray>",
            "GRAY",
            "GRAY",
            WoolColor.GRAY,
            StainedGlassColor.GRAY,
            DyeColor.GRAY),
    LIGHT_GRAY((byte) 8,
            org.bukkit.Color.fromRGB(142, 142, 134),
            ChatColor.GRAY,
            NamedTextColor.GRAY,
            "<gray>",
            "LIGHT_GRAY",
            "LIGHT_GRAY",
            WoolColor.LIGHT_GRAY,
            StainedGlassColor.LIGHT_GRAY,
            DyeColor.SILVER),
    CYAN((byte) 9,
            org.bukkit.Color.fromRGB(21, 137, 145),
            ChatColor.DARK_AQUA,
            NamedTextColor.DARK_AQUA,
            "<dark_aqua>",
            "CYAN",
            "CYAN",
            WoolColor.CYAN,
            StainedGlassColor.CYAN,
            DyeColor.CYAN),
    PURPLE((byte) 10,
            org.bukkit.Color.fromRGB(121, 42, 172),
            ChatColor.BLUE,
            NamedTextColor.BLUE,
            "<blue>",
            "PURPLE",
            "PURPLE",
            WoolColor.PURPLE,
            StainedGlassColor.PURPLE,
            DyeColor.PURPLE),
    BLUE((byte) 11,
            org.bukkit.Color.BLUE,
            ChatColor.DARK_BLUE,
            NamedTextColor.DARK_BLUE,
            "<dark_blue>",
            "BLUE",
            "BLUE",
            WoolColor.BLUE,
            StainedGlassColor.BLUE,
            DyeColor.BLUE),
    BROWN((byte) 12,
            org.bukkit.Color.fromRGB(114, 71, 40),
            ChatColor.DARK_RED,
            NamedTextColor.DARK_RED,
            "<dark_red>",
            "BROWN",
            "BROWN",
            WoolColor.BROWN,
            StainedGlassColor.BROWN,
            DyeColor.BROWN),
    GREEN((byte) 13,
            org.bukkit.Color.GREEN,
            ChatColor.DARK_GREEN,
            NamedTextColor.DARK_GREEN,
            "<dark_green>",
            "GREEN",
            "GREEN",
            WoolColor.GREEN,
            StainedGlassColor.GREEN,
            DyeColor.GREEN),
    RED((byte) 14,
            org.bukkit.Color.RED,
            ChatColor.RED,
            NamedTextColor.RED,
            "<red>",
            "RED",
            "RED",
            WoolColor.RED,
            StainedGlassColor.RED,
            DyeColor.RED),
    BLACK((byte) 15,
            org.bukkit.Color.BLACK,
            ChatColor.BLACK,
            NamedTextColor.BLACK,
            "<black>",
            "BLACK",
            "BLACK",
            WoolColor.BLACK,
            StainedGlassColor.BLACK,
            DyeColor.BLACK);

    private final byte data;
    private final org.bukkit.Color color;
    private final ChatColor chatColor;
    private final TextColor colorComponent;
    private final String miniCode;
    private final String prefix;
    private final String name;
    private final WoolColor woolColor;
    private final StainedGlassColor glassColor;
    private final DyeColor dyeColor;

    Color(byte data,
          org.bukkit.Color color,
          ChatColor chatColor,
          TextColor colorComponent,
          String miniCode,
          String prefix,
          String name,
          WoolColor woolColor,
          StainedGlassColor glassColor,
          DyeColor dyeColor) {
        this.data = data;
        this.color = color;
        this.chatColor = chatColor;
        this.colorComponent = colorComponent;
        this.miniCode = miniCode;
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

    public TextColor getColorComponent() {
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
