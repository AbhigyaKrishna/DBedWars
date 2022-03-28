package org.zibble.dbedwars.api.handler;

import org.zibble.dbedwars.api.hooks.Hook;
import org.zibble.dbedwars.api.hooks.nickname.NickNameHook;
import org.zibble.dbedwars.api.hooks.npc.NPCFactory;
import org.zibble.dbedwars.api.hooks.placholder.PlaceholderHook;
import org.zibble.dbedwars.api.hooks.scoreboard.ScoreboardHook;
import org.zibble.dbedwars.api.hooks.vanish.VanishHook;
import org.zibble.dbedwars.api.hooks.world.WorldAdaptor;

import java.util.Collection;
import java.util.function.Function;

public interface HookManager {

    WorldAdaptor getWorldAdaptor();

    void setWorldAdaptor(WorldAdaptor worldAdaptor);

    ScoreboardHook getScoreboardHook();

    void setScoreboardHook(ScoreboardHook scoreboardHook);

    MultiOptionalHook<VanishHook> getVanishHook();

    PlaceholderHook getPlaceholderHook();

    void setPlaceholderHook(PlaceholderHook placeholderHook);

    NPCFactory getNpcFactory();

    void setNpcFactory(NPCFactory npcFactory);

    MultiOptionalHook<NickNameHook> getNickNameHooks();

    interface MultiOptionalHook<T extends Hook> {

        <R, F> F perform(Function<T, R> testFunction, Function<Collection<R>, F> multiResultMapper, F defaultValue);

        void add(T hook);

        void remove(T hook);

    }

}
