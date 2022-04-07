package org.zibble.dbedwars.game.arena.traps;

import org.zibble.dbedwars.api.game.trap.Target;
import org.zibble.dbedwars.api.game.trap.TargetRegistry;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.game.arena.traps.targets.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TargetRegistryImpl implements TargetRegistry {

    private final Map<Key, Target> registeredTargets = Collections.synchronizedMap(new HashMap<>());

    public void registerDefaults() {
        this.register(new AllEnemy());
        this.register(new AllPlayers());
        this.register(new EnemyAtBase());
        this.register(new EnemyTeam());
        this.register(new RandomEnemyPlayer());
        this.register(new RandomTeamPlayer());
        this.register(new Team());
        this.register(new TeamAtBase());
        this.register(new TrapBuyer());
    }

    @Override
    public void register(Target target) {
        this.registeredTargets.put(target.getKey(), target);
    }

    @Override
    public void remove(Target target) {
        this.registeredTargets.get(target.getKey());
    }

    @Override
    public void remove(String key) {
        this.registeredTargets.remove(Key.of(key));
    }

    @Override
    public Target get(String key) {
        return this.registeredTargets.get(Key.of(key));
    }

    @Override
    public Collection<Target> getAll() {
        return Collections.unmodifiableCollection(this.registeredTargets.values());
    }

}
