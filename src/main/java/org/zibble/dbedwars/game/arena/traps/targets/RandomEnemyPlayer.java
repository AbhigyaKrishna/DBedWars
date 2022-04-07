package org.zibble.dbedwars.game.arena.traps.targets;

import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.trap.Target;
import org.zibble.dbedwars.api.game.trap.Trap;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.LenientKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class RandomEnemyPlayer implements Target {

    @Override
    public Collection<? extends ArenaPlayer> getTargets(Trap trap, ArenaPlayer trigger) {
        return Collections.singleton(new ArrayList<>(trigger.getTeam().getPlayers())
                .get(ThreadLocalRandom.current().nextInt(trigger.getTeam().getPlayers().size())));
    }

    @Override
    public Key getKey() {
        return LenientKey.of("random_enemy_player");
    }

}
