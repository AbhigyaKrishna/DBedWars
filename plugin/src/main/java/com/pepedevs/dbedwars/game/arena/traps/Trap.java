package com.pepedevs.dbedwars.game.arena.traps;

import com.pepedevs.dbedwars.action.ActionPreProcessor;
import com.pepedevs.dbedwars.action.ActionUtil;
import com.pepedevs.dbedwars.action.objects.ActionExecutor;
import com.pepedevs.dbedwars.action.objects.ProcessedActionHolder;
import com.pepedevs.dbedwars.api.action.ActionPlaceholder;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.util.Key;
import org.bukkit.entity.Player;

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
        for (com.pepedevs.dbedwars.api.game.trap.Trap.TrapAction value : this.actions.values()) {
            value.execute(value.getActionTarget(target));
        }
    }

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
                        List<ActionPlaceholder<?, ?>> placeholders = new ArrayList<>();
                        ActionPlaceholder<Void, ArenaPlayer> arenaPlayer = ActionUtil.getAsVoidPlaceholder(actionTarget);
                        ActionPlaceholder<Void, Player> vanillaPlayer = ActionUtil.getAsVoidPlaceholder(actionTarget.getPlayer());
                        placeholders.add(arenaPlayer);
                        placeholders.add(vanillaPlayer);
                        placeholders.addAll(Arrays.asList(this.getStandardPlaceholders(actionTarget)));
                        ProcessedActionHolder holder = ActionPreProcessor.process(executable, placeholders.toArray(new ActionPlaceholder[0]));
                        ActionExecutor.execute(holder);
                    }
                }
            }
        }

        private ActionPlaceholder<String, PlaceholderEntry>[] getStandardPlaceholders(ArenaPlayer arenaPlayer) {
            List<ActionPlaceholder<String, PlaceholderEntry>> placeholders = new ArrayList<>();
            return new ActionPlaceholder[] {
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("player_name", arenaPlayer.getPlayer().getName())),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("team_name", arenaPlayer.getTeam().getName())),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("team_color", arenaPlayer.getTeam().getColor().toString())),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("team_size", String.valueOf(arenaPlayer.getTeam().getPlayers().size()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("team_players_remaining", String.valueOf(arenaPlayer.getTeam().getPlayers().size()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("enemies_remaining", String.valueOf(arenaPlayer.getArena().getPlayers().size() - arenaPlayer.getTeam().getPlayers().size()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("x_block_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getBlockX()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("y_block_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getBlockY()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("z_block_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getBlockZ()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("x_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getX()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("y_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getY()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("z_coordinate", String.valueOf(arenaPlayer.getPlayer().getLocation().getZ()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("pitch", String.valueOf(arenaPlayer.getPlayer().getLocation().getPitch()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("yaw", String.valueOf(arenaPlayer.getPlayer().getLocation().getYaw()))),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("world_name", arenaPlayer.getPlayer().getWorld().getName())),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("world_environment", arenaPlayer.getPlayer().getWorld().getEnvironment().toString())),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("arena_display_name", arenaPlayer.getArena().getSettings().getCustomName())),
                    ActionUtil.getAsKeyedPlaceholder(PlaceholderEntry.symbol("arena_name", arenaPlayer.getArena().getSettings().getName()))
            };
        }
    }
}
