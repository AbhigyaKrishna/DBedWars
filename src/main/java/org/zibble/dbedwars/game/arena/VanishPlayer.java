package org.zibble.dbedwars.game.arena;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPlayer;
import io.github.retrooper.packetevents.util.SpigotDataHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.utils.Util;

import java.util.*;

public class VanishPlayer {

    public static final Map<UUID, VanishPlayer> INSTANCES = Collections.synchronizedMap(new HashMap<>());

    private final Player player;
    private final WrapperPlayServerPlayerInfo.PlayerData data;
    private boolean vanishFromAll;
    private final Set<UUID> vanishedFrom;

    public VanishPlayer(Player player) {
        this.player = player;
        UserProfile profile = Util.asProtocolProfile(DBedwars.getInstance().getNMSAdaptor().getProfile(this.getPlayer()));
        this.data = new WrapperPlayServerPlayerInfo.PlayerData(
                Component.text(this.getPlayer().getName()), // TODO: Name
                profile,
                SpigotDataHelper.fromBukkitGameMode(this.getPlayer().getGameMode()),
                PacketEvents.getAPI().getPlayerManager().getPing(this.getPlayer())
        );
        this.vanishedFrom = Collections.synchronizedSet(new HashSet<>());
        INSTANCES.put(this.player.getUniqueId(), this);
    }

    public boolean isVanishFromAll() {
        return this.vanishFromAll;
    }

    public void vanish() {
        this.vanish(Bukkit.getOnlinePlayers().toArray(new Player[0]));
        this.vanishFromAll = true;
    }

    public void vanish(Player... players) {
        WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, this.data);
        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(this.getPlayer().getEntityId());
        for (Player worldPlayer : players) {
            if (worldPlayer.equals(this.getPlayer()))
                continue;

            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, packet);
            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, infoPacket);
            this.vanishedFrom.add(worldPlayer.getUniqueId());
        }
    }

    public void vanishWithDeathAnimation() {
        this.vanishWithDeathAnimation(Bukkit.getOnlinePlayers().toArray(new Player[0]));
        this.vanishFromAll = true;
    }

    public void vanishWithDeathAnimation(Player... players) {
        WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, this.data);
        WrapperPlayServerEntityStatus packet = new WrapperPlayServerEntityStatus(this.getPlayer().getEntityId(), 3);
        for (Player worldPlayer : players) {
            if (worldPlayer.equals(this.getPlayer()))
                continue;

            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, packet);
            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, infoPacket);
            this.vanishedFrom.add(worldPlayer.getUniqueId());
        }
    }

    public void unVanish() {
        this.unVanish(this.getPlayer().getWorld().getPlayers().toArray(new Player[0]));
        this.vanishFromAll = false;
        this.vanishedFrom.clear();
    }

    public void unVanish(Player... players) {
        this.vanishFromAll = false;
        EntityData data = new EntityData(10, EntityDataTypes.BYTE, (byte) 0x7f);
        WrapperPlayServerSpawnPlayer packet = new WrapperPlayServerSpawnPlayer(this.getPlayer().getEntityId(), this.getPlayer().getUniqueId(),
                SpigotDataHelper.fromBukkitLocation(this.getPlayer().getLocation()), data);
        WrapperPlayServerPlayerInfo infoPacket = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, this.data);
        for (Player worldPlayer : players) {
            if (worldPlayer.equals(this.getPlayer()))
                continue;

            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, infoPacket);
            PacketEvents.getAPI().getPlayerManager().sendPacket(worldPlayer, packet);
            this.vanishedFrom.remove(worldPlayer.getUniqueId());
        }
        this.vanishFromAll = false;
    }

    public boolean canSee(Player player) {
        if (!this.isVanishFromAll()) return true;
        return !this.vanishedFrom.contains(player.getUniqueId());
    }

    public Player getPlayer() {
        return this.player;
    }

}
