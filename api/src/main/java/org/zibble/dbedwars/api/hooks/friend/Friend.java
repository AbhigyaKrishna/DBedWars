package org.zibble.dbedwars.api.hooks.friend;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Friend {

    UUID getUUID();

    String getName();

    boolean isOnline();

    default Player getPlayer() {
        return Bukkit.getPlayer(this.getUUID());
    }

}
