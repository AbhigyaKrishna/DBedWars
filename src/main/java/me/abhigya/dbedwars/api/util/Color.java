package me.abhigya.dbedwars.api.util;

import me.Abhigya.core.util.itemstack.stainedglass.StainedGlassColor;
import me.Abhigya.core.util.itemstack.wool.WoolColor;
import org.bukkit.ChatColor;

public enum Color {

    WHITE((byte) 0, org.bukkit.Color.fromRGB(233, 236, 236), ChatColor.WHITE, 'f', "WHITE", "WHITE", WoolColor.WHITE, StainedGlassColor.WHITE),
    ORANGE((byte) 1, org.bukkit.Color.fromRGB(240, 118, 19), ChatColor.GOLD, '6', "ORANGE", "ORANGE", WoolColor.ORANGE, StainedGlassColor.ORANGE),
    MAGENTA((byte) 2, org.bukkit.Color.fromRGB(189, 68, 179), ChatColor.DARK_PURPLE, '5', "MAGENTA", "MAGENTA", WoolColor.MAGENTA, StainedGlassColor.MAGENTA),
    AQUA((byte) 3, org.bukkit.Color.fromRGB(58, 175, 217), ChatColor.AQUA, 'b', "LIGHT_BLUE", "AQUA", WoolColor.LIGHT_BLUE, StainedGlassColor.LIGHT_BLUE),
    YELLOW((byte) 4, org.bukkit.Color.fromRGB(248, 198, 39), ChatColor.YELLOW, 'e', "YELLOW", "YELLOW", WoolColor.YELLOW, StainedGlassColor.YELLOW),
    LIME((byte) 5, org.bukkit.Color.fromRGB(112, 185, 25), ChatColor.GREEN, 'a', "LIME", "LIME", WoolColor.LIME, StainedGlassColor.LIME),
    PINK((byte) 6, org.bukkit.Color.fromRGB(237, 141, 172), ChatColor.LIGHT_PURPLE, 'd', "PINK", "PINK", WoolColor.PINK, StainedGlassColor.PINK),
    GRAY((byte) 7, org.bukkit.Color.fromRGB(62, 68, 71), ChatColor.DARK_GRAY, '8', "GRAY", "GRAY", WoolColor.GRAY, StainedGlassColor.GRAY),
    LIGHT_GRAY((byte) 8, org.bukkit.Color.fromRGB(142, 142, 134), ChatColor.GRAY, '7', "LIGHT_GRAY", "LIGHT_GRAY", WoolColor.LIGHT_GRAY, StainedGlassColor.LIGHT_GRAY),
    CYAN((byte) 9, org.bukkit.Color.fromRGB(21, 137, 145), ChatColor.DARK_AQUA, '3', "CYAN", "CYAN", WoolColor.CYAN, StainedGlassColor.CYAN),
    PURPLE((byte) 10, org.bukkit.Color.fromRGB(121, 42, 172), ChatColor.BLUE, '9', "PURPLE", "PURPLE", WoolColor.PURPLE, StainedGlassColor.PURPLE),
    BLUE((byte) 11, org.bukkit.Color.fromRGB(53, 57, 157), ChatColor.DARK_BLUE, '1', "BLUE", "BLUE", WoolColor.BLUE, StainedGlassColor.BLUE),
    BROWN((byte) 12, org.bukkit.Color.fromRGB(114, 71, 40), ChatColor.DARK_RED, '4', "BROWN", "BROWN", WoolColor.BROWN, StainedGlassColor.BROWN),
    GREEN((byte) 13, org.bukkit.Color.fromRGB(84, 109, 27), ChatColor.DARK_GREEN, '2', "GREEN", "GREEN", WoolColor.GREEN, StainedGlassColor.GREEN),
    RED((byte) 14, org.bukkit.Color.fromRGB(161, 39, 34), ChatColor.RED, 'c', "RED", "RED", WoolColor.RED, StainedGlassColor.RED),
    BLACK((byte) 15, org.bukkit.Color.fromRGB(20, 21, 25), ChatColor.BLACK, '0', "BLACK", "BLACK", WoolColor.BLACK, StainedGlassColor.BLACK);

    private final byte data;
    private final org.bukkit.Color color;
    private final ChatColor chatColor;
    private final char chatSymbol;
    private final String prefix;
    private final String name;
    private final WoolColor woolColor;
    private final StainedGlassColor glassColor;

    Color(byte data, org.bukkit.Color color, ChatColor chatColor, char chatSymbol, String prefix, String name, WoolColor woolColor, StainedGlassColor glassColor) {
        this.data = data;
        this.color = color;
        this.chatColor = chatColor;
        this.chatSymbol = chatSymbol;
        this.prefix = prefix;
        this.name = name; // TODO: change name according to language
        this.woolColor = woolColor;
        this.glassColor = glassColor;
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
}
