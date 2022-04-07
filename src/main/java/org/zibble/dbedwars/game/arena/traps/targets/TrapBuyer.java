package org.zibble.dbedwars.game.arena.traps.targets;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.trap.Target;
import org.zibble.dbedwars.api.game.trap.Trap;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.LenientKey;

import java.util.Collection;
import java.util.Collections;

public class TrapBuyer implements Target {

    @Override
    public Collection<? extends ArenaPlayer> getTargets(Trap trap, ArenaPlayer trigger) {
        return Collections.singleton(trap.getTrapBuyer());
    }

    @Override
    public Key getKey() {
        return LenientKey.of("trap_buyer");
    }

}
