package org.zibble.dbedwars.api.guis;

import com.google.common.base.Objects;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.zibble.inventoryframework.protocol.ProtocolPlayer;

import java.util.UUID;

public class MenuPlayer implements ProtocolPlayer<Player> {

    private final Player player;

    public MenuPlayer(Player player) {
        this.player = player;
    }

    public static MenuPlayer of(Player player) {
        return new MenuPlayer(player);
    }

    @Override
    public @NotNull UUID getUniqueID() {
        return player.getUniqueId();
    }

    @Override
    public @NotNull String getName() {
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MenuPlayer && ((MenuPlayer) obj).player.getUniqueId().equals(this.player.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(player.getUniqueId());
    }

}
