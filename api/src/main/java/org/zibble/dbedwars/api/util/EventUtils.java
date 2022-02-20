package org.zibble.dbedwars.api.util;

import org.bukkit.event.block.Action;

/** Class for dealing with events */
public class EventUtils {

    /**
     * Check for Right click
     *
     * <p>
     *
     * @param action {@link Action}
     * @return <code>true</code> if right click else false
     * @see Action
     */
    public static boolean isRightClick(Action action) {
        return action != null && (action.name().contains("RIGHT_"));
    }

    /**
     * Check for Left click
     *
     * <p>
     *
     * @param action {@link Action}
     * @return <code>true</code> if left click else false
     * @see Action
     */
    public static boolean isLeftClick(Action action) {
        return action != null && (action.name().contains("LEFT_"));
    }

    /**
     * Check for Clicking Block
     *
     * <p>
     *
     * @param action {@link Action}
     * @return <code>true</code> if clicking block else false
     * @see Action
     */
    public static boolean isClickingBlock(Action action) {
        return action != null && (action.name().contains("_BLOCK"));
    }

    /**
     * Check for Clicking Air
     *
     * <p>
     *
     * @param action {@link Action}
     * @return <code>true</code> if clicking air else false
     * @see Action
     */
    public static boolean isClickingAir(Action action) {
        return action != null && (action.name().contains("_AIR"));
    }
}
