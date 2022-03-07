package org.zibble.dbedwars.menus.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.inventoryframework.protocol.ProtocolPlayer;

import java.util.UUID;

public class MenuPlayer implements ProtocolPlayer<Player> {

    private final Player player;

    public static MenuPlayer of(Player player) {
        return new MenuPlayer(player);
    }

    public MenuPlayer(Player player) {
        this.player = player;
    }

    @Override
    public @NotNull UUID uuid() {
        return player.getUniqueId();
    }

    @Override
    public @NotNull String name() {
        return player.getName();
    }

    @Override
    public @NotNull Player handle() {
        return player;
    }

    @Override
    public void updatePlayerInventory() {
        player.updateInventory();
    }
}
