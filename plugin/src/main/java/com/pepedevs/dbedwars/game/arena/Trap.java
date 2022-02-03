package com.pepedevs.dbedwars.game.arena;

import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.util.TrapEnum;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableTrap;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class Trap implements com.pepedevs.dbedwars.api.game.Trap {

    private String id;
    private ConfigurableTrap cfgTrap;
    private TrapEnum.TriggerType triggerType;

    private Map<TrapEnum.TargetType, Set<Consumer<ArenaPlayer>>> actions;

    public Trap(String id, TrapEnum.TriggerType trigger) {
        this.id = id;
        this.triggerType = trigger;
        this.actions = new HashMap<>();
    }

    public Trap(ConfigurableTrap trap) {
        this(trap.getId(), trap.getTrigger());
        this.cfgTrap = trap;
        this.parseTrapAction();
    }

    public static Trap fromConfig(ConfigurableTrap config) {
        for (ConfigurableTrap.ConfigurableTrapAction action : config.getTrapActions()) {
            for (String executable : action.getExecutables()) {
            }
        }
    }

    @Override
    public void trigger(ArenaPlayer target, Team team) {
        for (Map.Entry<TrapEnum.TargetType, Set<Consumer<ArenaPlayer>>> entry : this.actions.entrySet()) {
            switch (entry.getKey()) {
                case TEAM: {
                    fulfill(team.getPlayers(), entry.getValue());
                    return;
                }
                case ENEMY_TEAM: {
                    fulfill(target.getTeam().getPlayers(), entry.getValue());
                    return;
                }
                case ENEMY_AT_BASE: {
                    fulfill(Collections.singleton(target), entry.getValue());
                    return;
                }
                case TEAM_AT_BASE: {
                    Set<ArenaPlayer> set = new HashSet<>();
                    for (ArenaPlayer player : team.getPlayers()) {
                        if (team.getIslandArea().contains(player.getPlayer().getLocation().toVector())) set.add(player);
                    }
                    fulfill(set, entry.getValue());
                    return;
                }
                case RANDOM_TEAM_PLAYER: {
                    fulfill(Collections.singleton(new ArrayList<>(team.getPlayers()).get(ThreadLocalRandom.current().nextInt(team.getPlayers().size()))), entry.getValue());
                    return;
                }
                case RANDOM_ENEMY_PLAYER: {
                    fulfill(Collections.singleton(new ArrayList<>(target.getTeam().getPlayers()).get(ThreadLocalRandom.current().nextInt(team.getPlayers().size()))), entry.getValue());
                    return;
                }
                case ALL_PLAYER: {
                    fulfill(target.getArena().getPlayers(), entry.getValue());
                    return;
                }
                case ALL_ENEMY: {
                    Set<ArenaPlayer> players = new HashSet<>();
                    for (Team team1 : target.getArena().getTeams()) {
                        if (!team.equals(team1)) players.addAll(team1.getPlayers());
                    }
                    fulfill(players, entry.getValue());
                }
            }
        }
    }

    private void fulfill(Collection<ArenaPlayer> arenaPlayers, Collection<Consumer<ArenaPlayer>> consumers) {
        for (ArenaPlayer player : arenaPlayers) {
            for (Consumer<ArenaPlayer> consumer : consumers) {
                consumer.accept(player);
            }
        }
    }

    private void parseTrapAction() {
        for (ConfigurableTrap.ConfigurableTrapAction action : cfgTrap.getTrapActions()) {
            TrapEnum.TargetType targetType = TrapEnum.TargetType.match(action.getTarget());
            Set<Consumer<ArenaPlayer>> actions = new HashSet<>();
            for (String executable : action.getExecutables()) {
                for (TrapEnum.ActionType value : TrapEnum.ActionType.VALUES) {
                    if (executable.startsWith("[" + value.name()))
                        actions.add(value.getAction(executable));
                }
            }
            this.actions.put(targetType, actions);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public TrapEnum.TriggerType getTriggerType() {
        return triggerType;
    }

    @Override
    public Map<TrapEnum.TargetType, Set<Consumer<ArenaPlayer>>> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        return "Trap{" +
                "id='" + id + '\'' +
                ", cfgTrap=" + cfgTrap +
                ", triggerType=" + triggerType +
                ", actions=" + actions +
                '}';
    }
}
