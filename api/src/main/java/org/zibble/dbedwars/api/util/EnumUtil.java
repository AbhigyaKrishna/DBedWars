package org.zibble.dbedwars.api.util;

public class EnumUtil {

    public static <T extends Enum<T>> T matchEnum(String s, T[] values) {
        for (T value : values) {
            if (value.name().equalsIgnoreCase(s)
                    || value.name().replace("_", "-").equalsIgnoreCase(s)
                    || value.name().replace("_", "").equalsIgnoreCase(s)) return value;
        }

        return null;
    }

}
