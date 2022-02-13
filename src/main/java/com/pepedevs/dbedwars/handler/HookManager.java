package com.pepedevs.dbedwars.handler;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import com.pepedevs.dbedwars.api.plugin.PluginDependence;
import com.pepedevs.dbedwars.hooks.defaults.scoreboard.ScoreboardHookImpl;
import com.pepedevs.dbedwars.hooks.featherboard.FeatherBoardHook;
import com.pepedevs.dbedwars.hooks.quickboard.QuickBoardHook;

public class HookManager implements com.pepedevs.dbedwars.api.handler.HookManager {

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
