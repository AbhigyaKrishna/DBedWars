package me.abhigya.dbedwars.utils;

import me.Abhigya.core.util.StringUtils;
import me.Abhigya.core.util.xseries.XMaterial;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.game.spawner.DropType;
import me.abhigya.dbedwars.api.util.BwItemStack;
import me.abhigya.dbedwars.api.util.LocationXYZ;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.Map;

public class ConfigurationUtils {

    public static BwItemStack parseItem(String str) {
        String[] s = str.split(":");
        BwItemStack item = new BwItemStack(XMaterial.matchXMaterial(s[0]).get().parseMaterial());
        if (s.length == 2) {
            try {
                item.setData(Short.parseShort(s[1]));
            } catch (NumberFormatException ignored) {}
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

    public static Map.Entry<DropType, LocationXYZ> parseSpawner(String s) {
        String[] str = s.split("#");
        for (DropType d : DBedwars.getInstance().getGameManager().getDropTypes()) {
            if (d.getConfigId().equals(str[0]))
                return new AbstractMap.SimpleEntry<>(d, LocationXYZ.valueOf(str[1]));
        }

        return null;
    }

}
