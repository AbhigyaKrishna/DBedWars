package org.zibble.dbedwars.utils.gamerule;

import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.zibble.dbedwars.api.util.Validable;

import java.util.HashSet;
import java.util.Set;

public class GameRule implements Validable {

    protected final GameRuleType type;
    protected final Object value;
    protected final Set<GameRule> parents;

    public GameRule(GameRuleType type, Object value, GameRule... parents) {
        Validate.notNull(type, "type cannot be null!");
        Validate.notNull(value, "value cannot be null!");
        Validate.isTrue(type.isSameDataType(value), "the specified type and value are incompatible!");

        this.type = type;
        this.value = value;
        this.parents = new HashSet<>();
        for (GameRule parent : parents) {
            if (parent != null && parent.isValid()) {
                this.parents.add(parent);
            }
        }
    }

    public GameRuleType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public Set<GameRule> getParents() {
        return parents;
    }

    public World apply(World world) {
        this.getType().apply(world, value);
        for (GameRule parent : this.parents) {
            parent.getType().apply(world, parent.getValue());
        }
        return world;
    }

    @Override
    public boolean isValid() {
        return this.getType() != null && this.getValue() != null && this.getType().isSameDataType(getValue());
    }
}
