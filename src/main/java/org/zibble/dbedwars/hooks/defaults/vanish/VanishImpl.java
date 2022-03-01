package org.zibble.dbedwars.hooks.defaults.vanish;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPlayer;
import io.github.retrooper.packetevents.utils.SpigotDataHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.hooks.vanish.VanishHook;
import org.zibble.dbedwars.utils.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VanishImpl implements VanishHook, Listener {

    private final Map<UUID, WrapperPlayServerPlayerInfo.PlayerData> vanished;

    public VanishImpl() {
        this.vanished = Collections.synchronizedMap(new HashMap<>());
        Bukkit.getServer().getPluginManager().registerEvents(this, DBedwars.getInstance());
    }

    @Override
    public boolean isVanished(@NotNull Player player) {
        return vanished.containsKey(player.getUniqueId());
    }

    @Override
    public void vanish(@NotNull Player player) {
        UserProfile profile = Utils.asProtocolProfile(DBedwars.getInstance().getNMSAdaptor().getProfile(player));
        WrapperPlayServerPlayerInfo.PlayerData data = new WrapperPlayServerPlayerInfo.PlayerData(
                Component.text(player.getName()), // TODO: Name
                profile,
                SpigotDataHelper.fromBukkitGameMode(player.getGameMode()),
                PacketEvents.getAPI().getPlayerManager().getPing(player)
        );
        WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, data);

        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(player.getEntityId());
        for (Player worldPlayer : player.getWorld().getPlayers()) {
            if (worldPlayer.equals(player))
                continue;

            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, packet);
            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, infoPacket);
        }
        vanished.put(player.getUniqueId(), data);
    }

    public void vanishWithDeathAnimation(@NotNull Player player) {
        UserProfile profile = Utils.asProtocolProfile(DBedwars.getInstance().getNMSAdaptor().getProfile(player));
        WrapperPlayServerPlayerInfo.PlayerData data = new WrapperPlayServerPlayerInfo.PlayerData(
                Component.text(player.getName()), // TODO: Name
                profile,
                SpigotDataHelper.fromBukkitGameMode(player.getGameMode()),
                PacketEvents.getAPI().getPlayerManager().getPing(player)
        );
        WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, data);

        WrapperPlayServerEntityStatus packet = new WrapperPlayServerEntityStatus(player.getEntityId(), 3);
        for (Player worldPlayer : player.getWorld().getPlayers()) {
            if (worldPlayer.equals(player))
                continue;

            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, packet);
            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, infoPacket);
        }
        vanished.put(player.getUniqueId(), data);
    }

    @Override
    public void unVanish(@NotNull Player player) {
        if (!this.isVanished(player)) return;

        EntityData data = new EntityData(10, EntityDataTypes.BYTE, (byte) 0x7f);
        WrapperPlayServerSpawnPlayer packet = new WrapperPlayServerSpawnPlayer(player.getEntityId(), player.getUniqueId(),
                SpigotDataHelper.fromBukkitLocation(player.getLocation()), data);
        WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, vanished.get(player.getUniqueId()));
        for (Player worldPlayer : player.getWorld().getPlayers()) {
            if (worldPlayer.equals(player))
                continue;

            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, infoPacket);
            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, packet);
        }
        vanished.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoinWorld(PlayerChangedWorldEvent event) {
        final Player joinedPlayer = event.getPlayer();
        for (Map.Entry<UUID, WrapperPlayServerPlayerInfo.PlayerData> entry : vanished.entrySet()) {
            final Player vanishedPlayer = Bukkit.getPlayer(entry.getKey());
            if (vanishedPlayer == null)
                continue;

            if (joinedPlayer.getWorld().equals(vanishedPlayer.getWorld())) {
                WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(vanishedPlayer.getEntityId());
                PacketEvents.getAPI().getPlayerManager().sendPacket(joinedPlayer, packet);
            }
        }
    }

    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (this.isVanished(player))
            vanished.remove(player.getUniqueId());
    }

    @EventHandler
    public void handlePlayerKick(PlayerKickEvent event) {
        final Player player = event.getPlayer();
        if (this.isVanished(player))
            vanished.remove(player.getUniqueId());
    }

}
