package org.zibble.dbedwars.hooks.defaults.vanish;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.hooks.vanish.VanishHook;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishImpl implements VanishHook, Listener {

    private final Set<UUID> vanished;

    public VanishImpl() {
        this.vanished = Collections.synchronizedSet(new HashSet<UUID>());
        Bukkit.getServer().getPluginManager().registerEvents(this, DBedwars.getInstance());
    }

    @Override
    public boolean isVanished(@NotNull Player player) {
        return vanished.contains(player.getUniqueId());
    }

    @Override
    public void vanish(@NotNull Player player) {
        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(player.getEntityId());
        for(Player worldPlayer : player.getWorld().getPlayers()){
            if(worldPlayer.equals(player))
                continue;

            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, packet);
        }
        vanished.add(player.getUniqueId());
    }

    @Override
    public void unVanish(@NotNull Player player) {
        WrapperPlayServerSpawnPlayer packet = new WrapperPlayServerSpawnPlayer(player.getEntityId(), player.getUniqueId(),
                new Location(player.getLocation().getX(),
                        player.getLocation().getY(),
                        player.getLocation().getZ(),
                        player.getLocation().getYaw(),
                        player.getLocation().getPitch()
                ));
        for(Player worldPlayer : player.getWorld().getPlayers()){
            if(worldPlayer.equals(player))
                continue;

            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, packet);
        }
        vanished.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoinWorld(PlayerChangedWorldEvent event){
        final Player joinedPlayer = event.getPlayer();
        for(UUID vanishedUUID : vanished){
            final Player vanishedPlayer = Bukkit.getPlayer(vanishedUUID);
            if(vanishedPlayer == null)
                continue;

            if(joinedPlayer.getWorld().equals(vanishedPlayer.getWorld())){
                WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(vanishedPlayer.getEntityId());
                PacketEvents.getAPI().getPlayerManager().sendPacket(joinedPlayer, packet);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        final Player player = event.getPlayer();
        if(isVanished(player))
            vanished.remove(player.getUniqueId());
    }
}
