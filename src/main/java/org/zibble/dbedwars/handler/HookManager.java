package org.zibble.dbedwars.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.hooks.placholder.PlaceholderHook;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import org.zibble.dbedwars.api.plugin.PluginDependence;
import org.zibble.dbedwars.configuration.PluginFiles;
import org.zibble.dbedwars.hooks.autonicker.AutoNickerHook;
import org.zibble.dbedwars.hooks.betternick.BetterNickHook;
import org.zibble.dbedwars.hooks.citizens.CitizensHook;
import org.zibble.dbedwars.hooks.cmi.CMIHook;
import org.zibble.dbedwars.hooks.defaults.placeholder.PlaceholderHookImpl;
import org.zibble.dbedwars.hooks.defaults.scoreboard.ScoreboardHookImpl;
import org.zibble.dbedwars.hooks.eazynick.EazyNickHook;
import org.zibble.dbedwars.hooks.featherboard.FeatherBoardHook;
import org.zibble.dbedwars.hooks.nametagedit.NameTagEditHook;
import org.zibble.dbedwars.hooks.nickapi.NickAPIHook;
import org.zibble.dbedwars.hooks.nicknamer.NickNamerHook;
import org.zibble.dbedwars.hooks.placeholderapi.PlaceholderAPIHook;
import org.zibble.dbedwars.hooks.pvplevels.PvPLevelsHook;
import org.zibble.dbedwars.hooks.quickboard.QuickBoardHook;
import org.zibble.dbedwars.hooks.slimeworldmanager.SlimeWorldManagerHook;
import org.zibble.dbedwars.hooks.tab.TabHook;
import org.zibble.dbedwars.hooks.vault.VaultHook;
import org.zibble.dbedwars.hooks.viphide.VIPHideHook;

import java.util.ArrayList;
import java.util.List;

public class HookManager implements org.zibble.dbedwars.api.handler.HookManager {

    private final DBedwars plugin;
    private PluginDependence[] dependencies;

    private ScoreboardHook scoreboardHook;
    private PlaceholderHook placeholderHook;
    private List<NickNameHook> nickNameHooks;

    public HookManager(DBedwars plugin) {
        this.plugin = plugin;
        this.dependencies = new PluginDependence[]{
                new AutoNickerHook(),
                new BetterNickHook(),
                new CitizensHook(),
                new CMIHook(),
                new EazyNickHook(),
                new FeatherBoardHook(),
                new NameTagEditHook(),
                new NickAPIHook(),
                new NickNamerHook(),
                new PlaceholderAPIHook(),
                new PvPLevelsHook(),
                new QuickBoardHook(),
                new SlimeWorldManagerHook(PluginFiles.SLIME_WORLD_MANAGER_HOOK, Bukkit.getWorld(this.plugin.getMainWorld())),
                new TabHook(),
                new VaultHook(),
                new VIPHideHook(),
        };
    }

    public void load() {
        this.scoreboardHook = new ScoreboardHookImpl();

        if (PlaceholderAPIHook.get().isEnabled())
            this.placeholderHook = new PlaceholderAPIHook();
        else
            this.placeholderHook = new PlaceholderHookImpl();

        this.nickNameHooks = new ArrayList<>();
        if (AutoNickerHook.get().isEnabled())
            nickNameHooks.add(AutoNickerHook.get());
        if (BetterNickHook.get().isEnabled())
            nickNameHooks.add(BetterNickHook.get());
        if (CMIHook.get().isEnabled())
            nickNameHooks.add(((CMIHook) CMIHook.get()).getCmiNick());
        if (EazyNickHook.get().isEnabled())
            nickNameHooks.add(EazyNickHook.get());
        if (NameTagEditHook.get().isEnabled())
            nickNameHooks.add(NameTagEditHook.get());
        if (NickAPIHook.get().isEnabled())
            nickNameHooks.add(NickAPIHook.get());
        if (NickNamerHook.get().isEnabled())
            nickNameHooks.add(NickNamerHook.get());
        if (VIPHideHook.get().isEnabled())
            nickNameHooks.add(VIPHideHook.get());
    }

    @Override
    public ScoreboardHook getScoreboardHook() {
        return this.scoreboardHook;
    }

    @Override
    public void setScoreboardHook(ScoreboardHook scoreboardHook) {
        this.scoreboardHook = scoreboardHook;
    }

    @Override
    public List<NickNameHook> getNickNameHooks() {
        return this.nickNameHooks;
    }

    @Override
    public boolean isNicked(Player player) {
        for (NickNameHook nickNameHook : this.nickNameHooks) {
            if (nickNameHook.isPlayerNicked(player))
                return true;
        }
        return false;
    }

    public PlaceholderHook getPlaceholderHook() {
        return this.placeholderHook;
    }

    public void setPlaceholderHook(PlaceholderHook placeholderHook) {
        this.placeholderHook = placeholderHook;
    }

    public PluginDependence[] getDependencies() {
        return this.dependencies;
    }

}
