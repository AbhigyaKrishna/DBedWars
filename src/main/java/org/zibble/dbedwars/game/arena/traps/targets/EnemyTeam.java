package org.zibble.dbedwars.game.arena.traps.targets;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.trap.Target;
import org.zibble.dbedwars.api.game.trap.Trap;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.LenientKey;

import java.util.Collection;

public class EnemyTeam implements Target {

    @Override
    public Collection<? extends ArenaPlayer> getTargets(Trap trap, ArenaPlayer trigger) {
        return trigger.getTeam().getPlayers();
    }

    @Override
    public Key getKey() {
        return LenientKey.of("enemy_team");
    }

}
