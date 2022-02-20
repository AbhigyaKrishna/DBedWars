package org.zibble.dbedwars.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Debugger {

    private static boolean ENABLED;
    private static final Logger LOGGER = Logger.getLogger("DBedWars");

    public static void debug(Object object) {
        debug(object.toString());
    }

    public static void debug(Object object, Level level) {
        debug(object.toString(), level);
    }

    public static void debug(int i) {
        debug(String.valueOf(i));
    }

    public static void debug(int i, Level level) {
        debug(String.valueOf(i), level);
    }

    public static void debug(boolean bool) {
        debug(String.valueOf(bool));
    }

    public static void debug(boolean bool, Level level) {
        debug(String.valueOf(bool), level);
    }

    public static void debug(long l) {
        debug(String.valueOf(l));
    }

    public static void debug(long l, Level level) {
        debug(String.valueOf(l), level);
    }

    public static void debug(double d) {
        debug(String.valueOf(d));
    }

    public static void debug(double d, Level level) {
        debug(String.valueOf(d), level);
    }

    public static void debug(float f) {
        debug(String.valueOf(f));
    }

    public static void debug(float f, Level level) {
        debug(String.valueOf(f), level);
    }

    public static void debug(byte b) {
        debug(String.valueOf(b));
    }

    public static void debug(byte b, Level level) {
        debug(String.valueOf(b), level);
    }

    public static void debug(String s) {
        if (ENABLED) LOGGER.info(s);
    }

    public static void debug(String s, Level level) {
        if (ENABLED) LOGGER.log(level, s);
    }

    public static void setEnabled(boolean enabled) {
        ENABLED = enabled;
    }
}
