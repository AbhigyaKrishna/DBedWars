package org.zibble.dbedwars.script.condition.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;
import org.zibble.dbedwars.api.script.condition.Condition;
import org.zibble.dbedwars.utils.Util;

import java.util.function.BiPredicate;

public class PlayerCondition<T> implements Condition {

    private BiPredicate<Player, T> predicate;
    private T value;
    private Player player;

    public PlayerCondition(BiPredicate<Player, T> predicate, T value, Player player) {
        this.predicate = predicate;
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean test() {
        return this.predicate.test(player, value);
    }

    public Player getPlayer() {
        return this.player;
    }

    public static class Type {

        public static final BiPredicate<Player, ItemStack[]> HAS_ITEM = (player, value) -> {
            boolean bool = true;
            for (ItemStack itemStack : value) {
                bool = bool && Util.playerHasItem(player, itemStack);
            }
            return bool;
        };

        public static final BiPredicate<Player, Double> HAS_EXP = (player, value) -> player.getExp() >= value;

        public static final BiPredicate<Player, String> HAS_PERMISSION = Permissible::hasPermission;

    }

}
