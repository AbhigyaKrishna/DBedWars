package com.pepedevs.dbedwars.utils;

import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.radium.utils.xseries.XMaterial;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.spawner.DropType;
import com.pepedevs.dbedwars.api.game.view.AttributeType;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.dbedwars.api.util.LocationXYZ;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ConfigurationUtils {

    public static <T extends Enum<T>> T matchEnum(String s, T[] values) {
        for (T value : values) {
            if (value.name().equalsIgnoreCase(s)
                    || value.name().replace("_", "-").equalsIgnoreCase(s)
                    || value.name().replace("_", "").equalsIgnoreCase(s)) return value;
        }

        return null;
    }

    public static BwItemStack parseItem(String str) {
        String[] s = str.split(":");
        BwItemStack item = new BwItemStack(XMaterial.matchXMaterial(s[0]).get().parseMaterial());
        if (s.length == 2) {
            try {
                item.setData(Short.parseShort(s[1]));
            } catch (NumberFormatException ignored) {
            }
        }

        return item;
    }

    public static String parseMessage(String str) {
        return StringUtils.translateAlternateColorCodes(str);
        // TODO: Parse Placeholders
    }

    public static String parsePlaceholder(String str, Player player) {
        return str;
    }

    public static String serializeSpawner(DropType drop, LocationXYZ location) {
        return drop.getConfigId() + "#" + location.toString();
    }

    public static String[][] parseGuiPattern(List<String> lines) {
        String[][] pattern = new String[Math.min(lines.size(), 6)][9];
        for (byte i = 0; i < lines.size(); i++) {
            pattern[i] = lines.get(i).split(" ");
        }
        return pattern;
    }

    public static Map.Entry<DropType, LocationXYZ> parseSpawner(String s) {
        String[] str = s.split("#");
        for (DropType d : DBedwars.getInstance().getGameManager().getDropTypes()) {
            if (d.getConfigId().equals(str[0]))
                return new AbstractMap.SimpleEntry<>(d, LocationXYZ.valueOf(str[1]));
        }

        return null;
    }

    public static BwItemStack parseItem(Color color, String s) {
        String replace = color == null ? "" : String.valueOf(color.getData());
        s = s.replace("%team%", replace);
        s = s.replace("STAINED_GLASS", "GLASS");
        s = s.replace("GLASS", "STAINED_GLASS");
        return BwItemStack.valueOf(s);
    }

    public static Set<ItemStack> parseCost(String s) {
        Set<ItemStack> items = new HashSet<>();
        String[] split = s.split(",");
        for (String str : split) {
            if (!str.contains(":")) continue;

            String[] t = str.split(":");
            int num;
            try {
                num = Integer.parseInt(t[0]);
            } catch (NumberFormatException e) {
                DBedwars.getInstance()
                        .getLogger()
                        .warning("Cost `" + s + "` is not in right format! Skipping it!");
                continue;
            }
            Optional<XMaterial> xm =
                    XMaterial.matchXMaterial(t.length > 2 ? t[1] + ":" + t[2] : t[1]);
            xm.ifPresent(
                    xMaterial -> {
                        ItemStack i = xMaterial.parseItem();
                        i.setAmount(num);
                        items.add(i);
                    });
        }

        return items;
    }

    public static Set<AttributeType> getAttributeTypes(String s) {
        Set<AttributeType> attributes = new HashSet<>();
        for (String str : s.split(",")) {
            AttributeType type = ConfigurationUtils.matchEnum(str, AttributeType.VALUES);
            if (type != null) attributes.add(type);
        }
        return attributes;
    }
}
