package org.zibble.dbedwars.script.condition.translator;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.game.ArenaPlayer;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.script.condition.impl.PlayerCondition;

public class PlayerConditionTranslator extends IConditionTranslator<Player, PlayerCondition<?>> {

    @Override
    public PlayerCondition<?> serialize(String key, String value, ScriptVariable<?>... variables) {
        Player player = null;
        for (ScriptVariable<?> variable : variables) {
            if (variable.isAssignableFrom(Player.class) || variable.getKey().get().equalsIgnoreCase("player")) {
                player = (Player) variable.value();
            } else if (variable.isAssignableFrom(ArenaPlayer.class) || variable.getKey().get().equalsIgnoreCase("arenaplayer")) {
                player = ((ArenaPlayer) variable.value()).getPlayer();
            }
        }

        if (player == null) return null;

        if (key.equalsIgnoreCase("has_item")) {
            // TODO parse item
            return new PlayerCondition<>(PlayerCondition.Type.HAS_ITEM, new ItemStack[0], player);
        } else if (key.equalsIgnoreCase("has_exp")) {
            return new PlayerCondition<>(PlayerCondition.Type.HAS_EXP, Double.parseDouble(value), player);
        } else if (key.equalsIgnoreCase("has_permission")) {
            return new PlayerCondition<>(PlayerCondition.Type.HAS_PERMISSION, value, player);
        }

        return null;
    }

    @Override
    public Class<? extends Player> getClazz() {
        return Player.class;
    }

    @Override
    public String deserialize(PlayerCondition<?> condition) {
        return condition.getValue().toString();
    }

    @Override
    public Key getKey() {
        return Key.of("PLAYER");
    }

}
