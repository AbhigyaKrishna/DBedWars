package org.zibble.dbedwars.game.arena.traps;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.zibble.dbedwars.api.events.TrapTriggerEvent;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.game.trap.Target;
import org.zibble.dbedwars.api.game.trap.Trap;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionProvider;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.script.ScriptRegistryImpl;

import java.util.*;

public class TrapImpl implements Trap {

    private final Key key;
    private final ArenaPlayer buyer;
    private TriggerType triggerType;

    private Set<TrapAction> actions;

    public TrapImpl(String key, ArenaPlayer buyer, TriggerType trigger) {
        this.key = Key.of(key);
        this.triggerType = trigger;
        this.buyer = buyer;
        this.actions = new HashSet<>();
    }

    @Override
    public Key getKey() {
        return this.key;
    }

    @Override
    public Team getTrapOwner() {
        return this.buyer.getTeam();
    }

    @Override
    public ArenaPlayer getTrapBuyer() {
        return this.buyer;
    }

    @Override
    public Set<TrapAction> getActions() {
        return this.actions;
    }

    @Override
    public void addAction(TrapAction action) {
        this.actions.add(action);
    }

    @Override
    public boolean trigger(ArenaPlayer trigger) {
        TrapTriggerEvent event = new TrapTriggerEvent(this, trigger);
        event.call();

        if (event.isCancelled()) return false;

        Cache<Target, Collection<? extends ArenaPlayer>> cache = CacheBuilder.newBuilder().build();
        for (TrapAction value : this.actions) {
            Target target = value.getTarget();
            if (target == null) continue;
            Collection<? extends ArenaPlayer> targets = cache.getIfPresent(target);
            if (targets == null) {
                targets = target.getTargets(this, trigger);
                cache.put(target, targets);
            }
            value.execute(targets);
        }
        cache.invalidateAll();

        return true;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public class TrapActionImpl implements TrapAction {

        private final Target target;
        private final ActionProvider provider;

        public TrapActionImpl(Target target, String action) {
            this.target = target;
            this.provider = ActionProvider.fromString(ScriptRegistryImpl.TRAP, action);
        }

        @Override
        public TrapImpl getTrap() {
            return TrapImpl.this;
        }

        @Override
        public Target getTarget() {
            return this.target;
        }

        @Override
        public void execute(Collection<? extends ArenaPlayer> targets) {
            for (ArenaPlayer target : targets) {
                List<ScriptVariable<?>> variables = new ArrayList<>();
                variables.add(ScriptVariable.of("ARENA_PLAYER", target));
                variables.add(ScriptVariable.of("PLAYER", target.getPlayer()));
                variables.addAll(Arrays.asList(this.getStandardPlaceholders(target)));
                this.provider.execute(variables.toArray(new ScriptVariable<?>[0]));
            }
        }

        private ScriptVariable<PlaceholderEntry>[] getStandardPlaceholders(ArenaPlayer arenaPlayer) {
            return new ScriptVariable[]{
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("player_name", arenaPlayer.getPlayer().getName())),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("team_name", arenaPlayer.getTeam().getName())),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("team_color", arenaPlayer.getTeam().getColor().toString())),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("team_size", String.valueOf(arenaPlayer.getTeam().getPlayers().size()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("team_players_remaining", String.valueOf(arenaPlayer.getTeam().getPlayers().size()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("enemies_remaining", String.valueOf(arenaPlayer.getArena().getPlayers().size() - arenaPlayer.getTeam().getPlayers().size()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("x_block_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getBlockX()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("y_block_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getBlockY()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("z_block_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getBlockZ()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("x_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getX()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("y_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getY()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("z_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getZ()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("pitch", String.valueOf(arenaPlayer.getPlayer().getLocation().getPitch()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("yaw", String.valueOf(arenaPlayer.getPlayer().getLocation().getYaw()))),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("world_name", arenaPlayer.getPlayer().getWorld().getName())),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("world_environment", arenaPlayer.getPlayer().getWorld().getEnvironment().toString())),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("arena_display_name", arenaPlayer.getArena().getDataHolder().getCustomName().getMessage())),
                    ScriptVariable.of("PLACEHOLDER", PlaceholderEntry.symbol("arena_name", arenaPlayer.getArena().getName()))
            };
        }

    }

}
