package org.zibble.dbedwars.script.condition.translator;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.util.Key;
import org.zibble.dbedwars.script.condition.impl.PlayerCondition;

public class PlayerConditionTranslator extends IConditionTranslator<Player, PlayerCondition<?>> {

    @Override
    public PlayerCondition<?> serialize(String key, String value, ScriptVariable<?>... variables) {
        if (key.equalsIgnoreCase("has_item")) {
            // TODO parse item
            return new PlayerCondition<>(PlayerCondition.Type.HAS_ITEM, new ItemStack[0]);
        } else if (key.equalsIgnoreCase("has_exp")) {
            return new PlayerCondition<>(PlayerCondition.Type.HAS_EXP, Double.parseDouble(value));
        } else if (key.equalsIgnoreCase("has_permission")) {
            return new PlayerCondition<>(PlayerCondition.Type.HAS_PERMISSION, value);
        }

        return null;
    }

    @Override
    public String deserialize(PlayerCondition<?> condition) {
        return condition.getValue().toString();
    }

    @Override
    public Key<String> getKey() {
        return Key.of("PLAYER");
    }

}
