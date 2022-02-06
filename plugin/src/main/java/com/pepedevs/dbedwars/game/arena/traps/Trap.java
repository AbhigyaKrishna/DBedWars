package com.pepedevs.dbedwars.game.arena.traps;

import com.pepedevs.dbedwars.action.ActionPreProcessor;
import com.pepedevs.dbedwars.action.objects.ProcessedActionHolder;
import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.task.DelayedTask;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.util.TrapEnum;
import com.pepedevs.radium.task.CancellableWorkload;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Trap implements com.pepedevs.dbedwars.api.game.trap.Trap {

    private Key<String> key;
    private final ArenaPlayer buyer;
    private TrapEnum.TriggerType triggerType;

    private Map<TrapEnum.TargetType, com.pepedevs.dbedwars.api.game.trap.Trap.TrapAction> actions;

    public Trap(String key, ArenaPlayer buyer, TrapEnum.TriggerType trigger, Set<TrapEnum.TargetType> targetTypes) {
        this.key = Key.of(key);
        this.triggerType = trigger;
        this.buyer = buyer;
        this.actions = new HashMap<>();
    }

    @Override
    public Key<String> getKey() {
        return this.key;
    }

//    public static Trap fromConfig(ConfigurableTrap config) {
//        for (ConfigurableTrap.ConfigurableTrapAction action : config.getTrapActions()) {
//            for (String executable : action.getExecutables()) {
//            }
//        }
//    }

    @Override
    public Team getTrapOwner() {
        return this.buyer.getTeam();
    }

    @Override
    public ArenaPlayer getTrapBuyer() {
        return this.buyer;
    }

    @Override
    public void trigger(ArenaPlayer target) {

    }

    private void parseTrapAction() {
//        for (ConfigurableTrap.ConfigurableTrapAction action : cfgTrap.getTrapActions()) {
//            TrapEnum.TargetType targetType = TrapEnum.TargetType.match(action.getTarget());
//            Set<Consumer<ArenaPlayer>> actions = new HashSet<>();
//            for (String executable : action.getExecutables()) {
//                for (TrapEnum.ActionType value : TrapEnum.ActionType.VALUES) {
//                    if (executable.startsWith("[" + value.name()))
//                        actions.add(value.getAction(executable));
//                }
//            }
//            this.actions.put(targetType, actions);
//        }
    }

    @Override
    public TrapEnum.TriggerType getTriggerType() {
        return triggerType;
    }

    public class TrapAction implements com.pepedevs.dbedwars.api.game.trap.Trap.TrapAction {

        private TrapEnum.TargetType targetType;
        private String[] actions;

        public TrapAction(TrapEnum.TargetType targetType, String[] actions) {
            this.targetType = targetType;
            this.actions = actions;
        }

        @Override
        public com.pepedevs.dbedwars.api.game.trap.Trap getTrap() {
            return Trap.this;
        }

        @Override
        public Collection<ArenaPlayer> getTarget(ArenaPlayer target) {
            switch (this.targetType) {
                case TRAP_BUYER: {
                    return Collections.singleton(this.getTrap().getTrapBuyer());
                }
                case TEAM: {
                    return this.getTrap().getTrapOwner().getPlayers();
                }
                case ENEMY_TEAM: {
                    return target.getTeam().getPlayers();
                }
                case ENEMY_AT_BASE: {
                    Set<ArenaPlayer> set = new HashSet<>();
                    for (ArenaPlayer player : target.getArena().getPlayers()) {
                        if (this.getTrap().getTrapOwner().getIslandArea().contains(player.getPlayer().getLocation().toVector()))
                            set.add(player);
                    }
                    return set;
                }
                case TEAM_AT_BASE: {
                    Set<ArenaPlayer> set = new HashSet<>();
                    for (ArenaPlayer player : this.getTrap().getTrapOwner().getPlayers()) {
                        if (this.getTrap().getTrapOwner().getIslandArea().contains(player.getPlayer().getLocation().toVector()))
                            set.add(player);
                    }
                    return set;
                }
                case RANDOM_TEAM_PLAYER: {
                    return Collections.singleton(new ArrayList<>(this.getTrap().getTrapOwner().getPlayers())
                            .get(ThreadLocalRandom.current().nextInt(this.getTrap().getTrapOwner().getPlayers().size())));
                }
                case RANDOM_ENEMY_PLAYER: {
                    return Collections.singleton(new ArrayList<>(target.getTeam().getPlayers())
                            .get(ThreadLocalRandom.current().nextInt(target.getTeam().getPlayers().size())));
                }
                case ALL_PLAYER: {
                    return this.getTrap().getTrapOwner().getArena().getPlayers();
                }
                case ALL_ENEMY: {
                    Set<ArenaPlayer> players = new HashSet<>();
                    for (Team team : target.getArena().getTeams()) {
                        players.addAll(team.getPlayers());
                    }
                    players.removeAll(this.getTrap().getTrapOwner().getPlayers());
                    return players;
                }
            }
            return Collections.emptyList();
        }

        @Override
        public void execute(Collection<ArenaPlayer> victim) {
            for (ArenaPlayer arenaPlayer : victim) {
                for (String executable : this.actions) {
                    ProcessedActionHolder<?> actionHolder = ActionPreProcessor.process(executable);
                    if (!actionHolder.shouldExecute()) continue;
                }
            }
        }

        private static

    }
}
