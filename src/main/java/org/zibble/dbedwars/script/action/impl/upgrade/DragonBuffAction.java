package org.zibble.dbedwars.script.action.impl.upgrade;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.Arena;
import org.zibble.dbedwars.api.game.GameEvent;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.NumberUtils;
import org.zibble.dbedwars.api.util.key.Key;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DragonBuffAction implements ActionTranslator<DragonBuffAction.Action> {

    public static final DragonBuffAction INSTANCE = new DragonBuffAction();

    private final Map<Team, Action> actions = new ConcurrentHashMap<>();

    @Override
    public Action serialize(String condition, ScriptVariable<?>... variables) {
        Team team = null;
        Arena arena = null;
        int level = 1;
        for (ScriptVariable<?> variable : variables) {
            if (variable.getKey().get().equals("TEAM")) {
                team = (Team) variable.value();
            } else if (variable.getKey().get().equals("ARENA")) {
                arena = (Arena) variable.value();
            }
        }

        String[] split = condition.trim().split(" ");
        GameEvent event = DBedwars.getInstance().getGameManager().getEvent(split[0]);
        if (split.length == 2) {
            level = NumberUtils.toInt(split[1], 1);
        }

        if (team == null || arena == null || event == null) return null;
        return new Action(arena, team, event, level);
    }

    @Override
    public String deserialize(Action condition) {
        return null;
    }

    @Override
    public Key getKey() {
        return Key.of("DRAGON_BUFF");
    }

    public Map<Team, Action> getActions() {
        return actions;
    }

    public static class Action implements org.zibble.dbedwars.api.script.action.Action {
        private final Arena arena;
        private final Team team;
        private final GameEvent event;
        private final int count;

        public Action(Arena arena, Team team, GameEvent event, int level) {
            this.arena = arena;
            this.team = team;
            this.event = event;
            this.count = level;
        }

        @Override
        public void execute() {
            DragonBuffAction.INSTANCE.actions.put(this.team, this);
        }

        public void spawnDragons() {
            Location location = this.arena.getDataHolder().getWaitingLocation().toBukkit(this.arena.getWorld());
            for (int i = 0; i < this.count; i++) {
                this.arena.getWorld().spawnEntity(location, EntityType.ENDER_DRAGON);
            }
        }

        public Arena getArena() {
            return arena;
        }

        public Team getTeam() {
            return team;
        }

        public GameEvent getEvent() {
            return event;
        }

    }

}
