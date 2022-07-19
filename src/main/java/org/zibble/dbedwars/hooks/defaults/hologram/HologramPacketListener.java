package org.zibble.dbedwars.hooks.defaults.hologram;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.api.util.mixin.ClickAction;
import org.zibble.dbedwars.api.util.ClickType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HologramPacketListener extends PacketListenerAbstract implements Listener {

    private final Cache<UUID, Integer> cooldown;
    private final HologramManager manager;


    protected HologramPacketListener(final HologramManager manager) {
        this.manager = manager;
        this.cooldown = CacheBuilder.newBuilder().expireAfterWrite(20, TimeUnit.MILLISECONDS).build();
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            Player player = (Player) event.getPlayer();

            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
            if (cooldown.asMap().containsKey(player.getUniqueId())
                    && cooldown.asMap().getOrDefault(player.getUniqueId(), -1)
                    == packet.getEntityId()) return;
            for (HologramImpl hologram : this.manager.getHolograms().values()) {
                if (!hologram.isClickRegistered()) continue;
                if (!hologram.getLocation().getWorld().getName().equals(player.getLocation().getWorld().getName()))
                    continue;
                Player clicker = (Player) event.getPlayer();
                HologramPageImpl page = (HologramPageImpl) hologram.getHologramPages().get(hologram.getViewerPages().getOrDefault(clicker.getUniqueId(), 0));
                boolean hologramMatch = false;
                for (HologramLine<?> line : page.getLines()) {
                    HologramLineImpl<?> hologramLine = (HologramLineImpl<?>) line;
                    for (int entityId : hologramLine.getEntityIds()) {
                        if (packet.getEntityId() == entityId) {
                            hologramMatch = true;
                            break;
                        }
                    }
                }
                if (!hologramMatch) continue;
                ClickType clickType = this.getClickType(clicker, packet.getAction());
                for (ClickAction clickAction : hologram.getClickActions()) {
                    clickAction.onClick(clicker, clickType);
                }
                for (ClickAction clickAction : page.getActions()) {
                    clickAction.onClick(clicker, clickType);
                }
                return;
            }
        }
    }

    private ClickType getClickType(Player player, WrapperPlayClientInteractEntity.InteractAction action) {
        if (action == WrapperPlayClientInteractEntity.InteractAction.ATTACK)
            return player.isSneaking() ? ClickType.SHIFT_LEFT_CLICK : ClickType.LEFT_CLICK;
        else
            return player.isSneaking() ? ClickType.SHIFT_RIGHT_CLICK : ClickType.RIGHT_CLICK;
    }

    @EventHandler
    public void handleMove(PlayerMoveEvent event) {
        for (HologramImpl hologram : this.manager.getHolograms().values()) {
            hologram.tracker.onMove(event);
        }
    }


}
