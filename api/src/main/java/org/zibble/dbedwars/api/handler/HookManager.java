package org.zibble.dbedwars.api.handler;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.hooks.placholder.PlaceholderHook;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;

import java.util.List;

public interface HookManager {

    ScoreboardHook getScoreboardHook();

    void setScoreboardHook(ScoreboardHook scoreboardHook);

    List<NickNameHook> getNickNameHooks();

    boolean isNicked(Player player);

    PlaceholderHook getPlaceholderHook();

    void setPlaceholderHook(PlaceholderHook placeholderHook);

}
