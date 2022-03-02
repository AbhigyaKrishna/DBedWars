package org.zibble.dbedwars.api.handler;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.hooks.placholder.PlaceholderHook;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import org.zibble.dbedwars.api.hooks.vanish.VanishHook;
import org.zibble.dbedwars.api.hooks.world.WorldAdaptor;

import java.util.List;

public interface HookManager {

    WorldAdaptor getWorldAdaptor();

    void setWorldAdaptor(WorldAdaptor worldAdaptor);

    ScoreboardHook getScoreboardHook();

    void setScoreboardHook(ScoreboardHook scoreboardHook);

    VanishHook getVanishHook();

    void setVanishHook(VanishHook vanishHook);

    PlaceholderHook getPlaceholderHook();

    void setPlaceholderHook(PlaceholderHook placeholderHook);

    List<NickNameHook> getNickNameHooks();

    boolean isNicked(Player player);

}
