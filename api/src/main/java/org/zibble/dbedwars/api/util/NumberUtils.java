package org.zibble.dbedwars.api.util;

import com.google.common.primitives.*;

public class NumberUtils {

    public static int toInt(String string) {
        return toInt(string, 0);
    }

    public static int toInt(String string, int defaultValue) {
        if (string == null) return defaultValue;
        Integer val = Ints.tryParse(string);
        return val == null ? defaultValue : val;
    }

    public static short toShort(String string) {
        return toShort(string, (short) 0);
    }

    public static short toShort(String string, short defaultValue) {
        if (string == null) return defaultValue;
        Integer val = Ints.tryParse(string);
        return val == null ? defaultValue : Shorts.saturatedCast(val);
    }

    public static long toLong(String string) {
        return toLong(string, 0);
    }

    public static long toLong(String string, long defaultValue) {
        if (string == null) return defaultValue;
        Long val = Longs.tryParse(string);
        return val == null ? defaultValue : val;
    }

    public static float toFloat(String string) {
        return toFloat(string, 0);
    }

    public static float toFloat(String string, float defaultValue) {
        if (string == null) return defaultValue;
        Float val = Floats.tryParse(string);
        return val == null ? defaultValue : val;
    }

    public static double toDouble(String string) {
        return toDouble(string, 0);
    }

    public static double toDouble(String string, double defaultValue) {
        if (string == null) return defaultValue;
        Double val = Doubles.tryParse(string);
        return val == null ? defaultValue : val;
    }

}
