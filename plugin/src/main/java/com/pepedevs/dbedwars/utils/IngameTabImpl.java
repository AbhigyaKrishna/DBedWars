package com.pepedevs.dbedwars.utils;

import me.Abhigya.core.handler.PluginHandler;
import me.Abhigya.core.util.packet.packetevents.PacketEvents;
import me.Abhigya.core.util.packet.packetevents.packetwrappers.play.out.playerinfo.WrappedPacketOutPlayerInfo;
import me.Abhigya.core.util.packet.packetevents.utils.gameprofile.WrappedGameProfile;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.game.Arena;
import com.pepedevs.dbedwars.api.game.ArenaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class IngameTabImpl extends PluginHandler {

    private final Arena arena;

    public IngameTabImpl(DBedwars plugin, Arena arena) {
        super(plugin);
        this.arena = arena;
    }

    @Override
    protected boolean isAllowMultipleInstances() {
        return false;
    }

    public IngameTabImpl clearAllPlayers(Player user) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            WrappedPacketOutPlayerInfo packet =
                    new WrappedPacketOutPlayerInfo(
                            WrappedPacketOutPlayerInfo.PlayerInfoAction.REMOVE_PLAYER,
                            new WrappedPacketOutPlayerInfo.PlayerInfo(
                                    player.getName(),
                                    new WrappedGameProfile(player.getUniqueId(), player.getName()),
                                    GameMode.CREATIVE,
                                    20));
            PacketEvents.get().getPlayerUtils().sendPacket(user, packet);
        }

        return this;
    }

    public IngameTabImpl showArenaPlayers(Player user) {
        for (ArenaPlayer player : this.arena.getPlayers()) {
            WrappedPacketOutPlayerInfo packet =
                    new WrappedPacketOutPlayerInfo(
                            WrappedPacketOutPlayerInfo.PlayerInfoAction.ADD_PLAYER,
                            new WrappedPacketOutPlayerInfo.PlayerInfo(
                                    player.getPlayer().getName(),
                                    new WrappedGameProfile(
                                            player.getPlayer().getUniqueId(),
                                            player.getPlayer().getName()),
                                    player.getPlayer().getGameMode(),
                                    PacketEvents.get()
                                            .getPlayerUtils()
                                            .getPing(player.getPlayer())));
            PacketEvents.get().getPlayerUtils().sendPacket(user, packet);
        }

        return this;
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {
        Optional<ArenaPlayer> opt = this.arena.getAsArenaPlayer(event.getPlayer());
        if (opt.isEmpty()) {
            WrappedPacketOutPlayerInfo packet =
                    new WrappedPacketOutPlayerInfo(
                            WrappedPacketOutPlayerInfo.PlayerInfoAction.REMOVE_PLAYER,
                            new WrappedPacketOutPlayerInfo.PlayerInfo(
                                    event.getPlayer().getName(),
                                    new WrappedGameProfile(
                                            event.getPlayer().getUniqueId(),
                                            event.getPlayer().getName()),
                                    GameMode.CREATIVE,
                                    20));

            for (ArenaPlayer player : this.arena.getPlayers()) {
                PacketEvents.get().getPlayerUtils().sendPacket(player.getPlayer(), packet);
            }
        }
    }
}
