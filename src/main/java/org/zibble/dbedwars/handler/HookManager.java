package org.zibble.dbedwars.handler;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import org.zibble.dbedwars.api.plugin.PluginDependence;
import org.zibble.dbedwars.hooks.defaults.scoreboard.ScoreboardHookImpl;
import org.zibble.dbedwars.hooks.featherboard.FeatherBoardHook;
import org.zibble.dbedwars.hooks.quickboard.QuickBoardHook;

public class HookManager implements org.zibble.dbedwars.api.handler.HookManager {

    private final DBedwars plugin;
    private PluginDependence[] dependencies;

    private ScoreboardHook scoreboardHook;

    public HookManager(DBedwars plugin) {
        this.plugin = plugin;
        this.dependencies = new PluginDependence[]{
                new FeatherBoardHook(),
                new QuickBoardHook()
        };
    }

    public void load() {
        if (QuickBoardHook.get().isEnabled()) {
            this.scoreboardHook = QuickBoardHook.get();
        } else {
            this.scoreboardHook = new ScoreboardHookImpl();
        }
    }

    @Override
    public ScoreboardHook getScoreboardHook() {
        return scoreboardHook;
    }

    @Override
    public void setScoreboardHook(ScoreboardHook scoreboardHook) {
        this.scoreboardHook = scoreboardHook;
    }

    public PluginDependence[] getDependencies() {
        return this.dependencies;
    }

}
