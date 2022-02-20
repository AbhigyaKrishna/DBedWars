package org.zibble.dbedwars.utils.gamerule;

import org.bukkit.World;

public class GameRuleDisableDaylightCycle extends GameRule {

    public static final long DEFAULT_FINAL_TIME = 500L;

    private final long permanent_time;

    public GameRuleDisableDaylightCycle() {
        this(GameRuleDisableDaylightCycle.DEFAULT_FINAL_TIME);
    }

    public GameRuleDisableDaylightCycle(long permanent_time) {
        super(GameRuleType.DAYLIGHT_CYCLE, false);
        this.permanent_time = permanent_time;
    }

    @Override
    public World apply(World world) {
        super.apply(world);
        world.setTime(permanent_time);
        return world;
    }

}
