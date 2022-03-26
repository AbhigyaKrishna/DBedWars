package org.zibble.dbedwars.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.zibble.dbedwars.game.arena.VanishPlayer;

import java.util.Map;
import java.util.UUID;

public class VanishListener implements Listener {

    @EventHandler
    public void onPlayerJoinWorld(PlayerChangedWorldEvent event) {
        for (Map.Entry<UUID, VanishPlayer> entry : VanishPlayer.INSTANCES.entrySet()) {
            if (entry.getKey().equals(event.getPlayer().getUniqueId())) {
                if (entry.getValue().isVanishFromAll()) {
                    entry.getValue().vanish();
                }
            } else if (entry.getValue().getPlayer().getWorld().equals(event.getPlayer().getWorld())
                    && (entry.getValue().isVanishFromAll() || !entry.getValue().canSee(event.getPlayer()))) {
                entry.getValue().vanish(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        for (Map.Entry<UUID, VanishPlayer> entry : VanishPlayer.INSTANCES.entrySet()) {
            if (entry.getKey().equals(event.getPlayer().getUniqueId())) {
                VanishPlayer.INSTANCES.remove(entry.getKey());
                break;
            }
        }
    }

    @EventHandler
    public void handlePlayerKick(PlayerKickEvent event) {
        for (Map.Entry<UUID, VanishPlayer> entry : VanishPlayer.INSTANCES.entrySet()) {
            if (entry.getKey().equals(event.getPlayer().getUniqueId())) {
                VanishPlayer.INSTANCES.remove(entry.getKey());
                break;
            }
        }
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        for (VanishPlayer value : VanishPlayer.INSTANCES.values()) {
            if (value.isVanishFromAll()) {
                value.vanish(event.getPlayer());
            }
        }
    }

}
