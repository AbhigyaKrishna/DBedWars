package org.zibble.dbedwars.handler;

import org.bukkit.Bukkit;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.handler.HookManager;
import org.zibble.dbedwars.api.hooks.Hook;
import org.zibble.dbedwars.api.hooks.hologram.HologramFactory;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.hooks.npc.NPCFactory;
import org.zibble.dbedwars.api.hooks.placholder.PlaceholderHook;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import org.zibble.dbedwars.api.hooks.selection.AreaSelectionHook;
import org.zibble.dbedwars.api.hooks.vanish.VanishHook;
import org.zibble.dbedwars.api.hooks.world.WorldAdaptor;
import org.zibble.dbedwars.api.plugin.PluginDependence;
import org.zibble.dbedwars.api.util.mixin.Initializable;
import org.zibble.dbedwars.configuration.PluginFiles;
import org.zibble.dbedwars.hooks.autonicker.AutoNickerHook;
import org.zibble.dbedwars.hooks.betternick.BetterNickHook;
import org.zibble.dbedwars.hooks.citizens.CitizensHook;
import org.zibble.dbedwars.hooks.cmi.CMIHook;
import org.zibble.dbedwars.hooks.defaults.hologram.HologramManager;
import org.zibble.dbedwars.hooks.defaults.npc.NPCFactoryImpl;
import org.zibble.dbedwars.hooks.defaults.placeholder.PlaceholderHookImpl;
import org.zibble.dbedwars.hooks.defaults.scoreboard.ScoreboardHookImpl;
import org.zibble.dbedwars.hooks.defaults.selection.AreaSelectionHookImpl;
import org.zibble.dbedwars.hooks.defaults.world.WorldAdaptorImpl;
import org.zibble.dbedwars.hooks.eazynick.EazyNickHook;
import org.zibble.dbedwars.hooks.featherboard.FeatherBoardHook;
import org.zibble.dbedwars.hooks.nametagedit.NameTagEditHook;
import org.zibble.dbedwars.hooks.nickapi.NickAPIHook;
import org.zibble.dbedwars.hooks.nicknamer.NickNamerHook;
import org.zibble.dbedwars.hooks.placeholderapi.PlaceholderAPIHook;
import org.zibble.dbedwars.hooks.premiumvanish.PremiumVanishHook;
import org.zibble.dbedwars.hooks.pvplevels.PvPLevelsHook;
import org.zibble.dbedwars.hooks.quickboard.QuickBoardHook;
import org.zibble.dbedwars.hooks.skinrestorer.SkinRestorerHook;
import org.zibble.dbedwars.hooks.slimeworldmanager.SlimeWorldManagerHook;
import org.zibble.dbedwars.hooks.supervanish.SuperVanishHook;
import org.zibble.dbedwars.hooks.tab.TabHook;
import org.zibble.dbedwars.hooks.vault.VaultHook;
import org.zibble.dbedwars.hooks.viphide.VIPHideHook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Function;

public class HookManagerImpl implements HookManager {

    private final DBedwars plugin;
    private PluginDependence[] dependencies;

    private WorldAdaptor worldAdaptor;
    private ScoreboardHook scoreboardHook;
    private PlaceholderHook placeholderHook;
    private NPCFactory npcFactory;
    private HologramFactory hologramFactory;
    private AreaSelectionHook areaSelectionHook;
    private MultiOptionalHookImpl<VanishHook> vanishHook;
    private MultiOptionalHookImpl<NickNameHook> nickNameHooks;
    private SkinRestorerHook skinRestorerHook;

    public HookManagerImpl(DBedwars plugin) {
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
                new PremiumVanishHook(),
                new PvPLevelsHook(),
                new QuickBoardHook(),
                new SkinRestorerHook(),
//                new SlimeWorldManagerHook(PluginFiles.SLIME_WORLD_MANAGER_HOOK, Bukkit.getWorld(this.plugin.getMainWorld())),
                new SuperVanishHook(),
                new TabHook(),
                new VaultHook(),
                new VIPHideHook(),
        };
    }

    public void load() {
//        if (this.getDependency(SlimeWorldManagerHook.class).isEnabled())
//            this.worldAdaptor = SlimeWorldManagerHook.get();
//        else
        this.worldAdaptor = new WorldAdaptorImpl(this.plugin);

        this.scoreboardHook = new ScoreboardHookImpl();
        this.skinRestorerHook = this.getDependency(SkinRestorerHook.class);

        if (this.getDependency(PlaceholderAPIHook.class).isEnabled())
            this.placeholderHook = new PlaceholderAPIHook();
        else
            this.placeholderHook = new PlaceholderHookImpl();

        this.npcFactory = new NPCFactoryImpl();
        this.hologramFactory = new HologramManager();
        this.areaSelectionHook = new AreaSelectionHookImpl();

        this.vanishHook = new MultiOptionalHookImpl<>();
        this.nickNameHooks = new MultiOptionalHookImpl<>();

        if (this.getDependency(AutoNickerHook.class).isEnabled())
            nickNameHooks.add(this.getDependency(AutoNickerHook.class));
        if (this.getDependency(BetterNickHook.class).isEnabled())
            nickNameHooks.add(this.getDependency(BetterNickHook.class));
        if (this.getDependency(EazyNickHook.class).isEnabled())
            nickNameHooks.add(this.getDependency(EazyNickHook.class));
        if (this.getDependency(NameTagEditHook.class).isEnabled())
            nickNameHooks.add(this.getDependency(NameTagEditHook.class));
        if (this.getDependency(NickAPIHook.class).isEnabled())
            nickNameHooks.add(this.getDependency(NickAPIHook.class));
        if (this.getDependency(NickNamerHook.class).isEnabled())
            nickNameHooks.add(this.getDependency(NickNamerHook.class));
        if (this.getDependency(VIPHideHook.class).isEnabled())
            nickNameHooks.add(this.getDependency(VIPHideHook.class));

        if (this.getDependency(PremiumVanishHook.class).isEnabled())
            vanishHook.add(this.getDependency(PremiumVanishHook.class));
        if (this.getDependency(SuperVanishHook.class).isEnabled())
            vanishHook.add(this.getDependency(SuperVanishHook.class));

        if (this.getDependency(CMIHook.class).isEnabled()) {
            CMIHook hook = this.getDependency(CMIHook.class);
            nickNameHooks.add(hook.getCmiNick());
            vanishHook.add(hook.getCmiVanish());
        }

        this.worldAdaptor.init();
        this.scoreboardHook.init();
        this.placeholderHook.init();
        this.npcFactory.init();
        this.hologramFactory.init();
        this.areaSelectionHook.init();
        this.vanishHook.init();
        this.nickNameHooks.init();

        if (this.skinRestorerHook.isEnabled())
            this.scoreboardHook.init();
    }

    public <T extends PluginDependence>  T getDependency(Class<T> clazz) {
        for (PluginDependence dependence : this.dependencies) {
            if (dependence.getClass().equals(clazz))
                return (T) dependence;
        }
        return null;
    }

    @Override
    public WorldAdaptor getWorldAdaptor() {
        return worldAdaptor;
    }

    @Override
    public void setWorldAdaptor(WorldAdaptor worldAdaptor) {
        synchronized (this) {
            this.worldAdaptor.disable();
        }
        this.worldAdaptor = worldAdaptor;
        this.worldAdaptor.init();
    }

    @Override
    public ScoreboardHook getScoreboardHook() {
        return this.scoreboardHook;
    }

    @Override
    public void setScoreboardHook(ScoreboardHook scoreboardHook) {
        synchronized (this) {
            this.scoreboardHook.disable();
        }
        this.scoreboardHook = scoreboardHook;
        this.scoreboardHook.init();
    }

    @Override
    public MultiOptionalHook<VanishHook> getVanishHook() {
        return vanishHook;
    }


    @Override
    public PlaceholderHook getPlaceholderHook() {
        return this.placeholderHook;
    }

    @Override
    public void setPlaceholderHook(PlaceholderHook placeholderHook) {
        synchronized (this) {
            this.placeholderHook.disable();
        }
        this.placeholderHook = placeholderHook;
        this.placeholderHook.init();
    }

    @Override
    public NPCFactory getNpcFactory() {
        return npcFactory;
    }

    @Override
    public void setNpcFactory(NPCFactory npcFactory) {
        synchronized (this) {
            this.npcFactory.disable();
        }
        this.npcFactory = npcFactory;
        this.npcFactory.init();
    }

    @Override
    public HologramFactory getHologramFactory() {
        return hologramFactory;
    }

    @Override
    public void setHologramFactory(HologramFactory hologramFactory) {
        synchronized (this) {
            this.hologramFactory.disable();
        }
        this.hologramFactory = hologramFactory;
        this.hologramFactory.init();
    }

    @Override
    public AreaSelectionHook getAreaSelectionHook() {
        return areaSelectionHook;
    }

    @Override
    public void setAreaSelectionHook(AreaSelectionHook areaSelectionHook) {
        synchronized (this) {
            this.areaSelectionHook.disable();
        }
        this.areaSelectionHook = areaSelectionHook;
        this.areaSelectionHook.init();
    }

    @Override
    public MultiOptionalHook<NickNameHook> getNickNameHooks() {
        return this.nickNameHooks;
    }

    public SkinRestorerHook getSkinRestorerHook() {
        return skinRestorerHook;
    }

    public PluginDependence[] getDependencies() {
        return this.dependencies;
    }

    public static class MultiOptionalHookImpl<T extends Hook> implements MultiOptionalHook<T>, Initializable {

        private final Collection<T> hooks;
        private boolean initialized;

        public MultiOptionalHookImpl() {
            this.hooks = Collections.synchronizedSet(new HashSet<>());
        }

        @Override
        public <R, F> F perform(Function<T, R> testFunction, Function<Collection<R>, F> multiResultMapper, F defaultValue) {
            if (this.hooks.isEmpty())
                return defaultValue;
            Collection<R> results = new ArrayList<>();
            for (T hook : this.hooks) {
                results.add(testFunction.apply(hook));
            }
            return multiResultMapper.apply(results);
        }

        @Override
        public void add(T hook) {
            if (this.isInitialized()) {
                hook.init();
            }
            this.hooks.add(hook);
        }

        @Override
        public void remove(T hook) {
            this.hooks.remove(hook);
            hook.disable();
        }

        public void init() {
            for (T hook : this.hooks) {
                hook.init();
            }
            this.initialized = true;
        }

        @Override
        public boolean isInitialized() {
            return this.initialized;
        }

    }

}
