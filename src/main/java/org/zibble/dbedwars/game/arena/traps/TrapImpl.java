package org.zibble.dbedwars.game.arena.traps;

import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.script.action.ActionPreProcessor;
import org.zibble.dbedwars.script.action.ActionProcessor;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.util.Key;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TrapImpl implements org.zibble.dbedwars.api.game.trap.Trap {

    private Key<String> key;
    private final ArenaPlayer buyer;
    private TrapEnum.TriggerType triggerType;

    private Map<TrapEnum.TargetType, org.zibble.dbedwars.api.game.trap.Trap.TrapAction> actions;

    public TrapImpl(String key, ArenaPlayer buyer, TrapEnum.TriggerType trigger, Set<TrapEnum.TargetType> targetTypes) {
        this.key = Key.of(key);
        this.triggerType = trigger;
        this.buyer = buyer;
        this.actions = new HashMap<>();
    }

    @Override
    public Key<String> getKey() {
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
    public void trigger(ArenaPlayer target) {
        for (org.zibble.dbedwars.api.game.trap.Trap.TrapAction value : this.actions.values()) {
            value.execute(value.getActionTarget(target));
            break;
        }
    }

    public TrapEnum.TriggerType getTriggerType() {
        return triggerType;
    }

    public class TrapAction implements org.zibble.dbedwars.api.game.trap.Trap.TrapAction {

        private TrapEnum.TargetType targetType;
        private String[] actions;

        public TrapAction(TrapEnum.TargetType targetType, String[] actions) {
            this.targetType = targetType;
            this.actions = actions;
        }

        @Override
        public org.zibble.dbedwars.api.game.trap.Trap getTrap() {
            return TrapImpl.this;
        }

        @Override
        public Collection<ArenaPlayer> getActionTarget(ArenaPlayer target) {
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
        public void execute(Collection<ArenaPlayer> targets) {
            for (ArenaPlayer target : targets) {
                for (String executable : this.actions) {
                    for (ArenaPlayer actionTarget : this.getActionTarget(target)) {
                        List<ScriptVariable<?>> variables = new ArrayList<>();
                        variables.add(ScriptVariable.of("ARENA_PLAYER", actionTarget));
                        variables.add(ScriptVariable.of("PLAYER", actionTarget.getPlayer()));
                        variables.addAll(Arrays.asList(this.getStandardPlaceholders(actionTarget)));
                        ActionProcessor holder = ActionPreProcessor.process(executable, variables.toArray(new ScriptVariable<?>[0]));
                        holder.execute();
                    }
                }
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
