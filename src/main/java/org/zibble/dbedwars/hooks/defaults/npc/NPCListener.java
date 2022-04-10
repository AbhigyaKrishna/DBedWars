package org.zibble.dbedwars.hooks.defaults.npc;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.zibble.dbedwars.api.util.mixin.ClickAction;
import org.zibble.dbedwars.api.util.ClickType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NPCListener extends PacketListenerAbstract implements Listener {

    private static final Cache<UUID, Integer> COOLDOWN =
            CacheBuilder.newBuilder().expireAfterWrite(20, TimeUnit.MILLISECONDS).build();

    private final NPCFactoryImpl factory;

    public NPCListener(NPCFactoryImpl factory) {
        super(PacketListenerPriority.MONITOR);
        this.factory = factory;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            Player player = (Player) event.getPlayer();
            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
            if (COOLDOWN.asMap().containsKey(player.getUniqueId())
                    && COOLDOWN.asMap().getOrDefault(player.getUniqueId(), -1) == packet.getEntityId()) return;
            for (BedWarsNPCImpl npc : factory.getNpcs().values()) {
                if (npc.getEntityID() == packet.getEntityId()) {
                    if (!npc.getLocation().getWorld().getName().equals(player.getLocation().getWorld().getName()))
                        continue;
                    for (ClickAction clickAction : npc.getClickActions()) {
                        clickAction.onClick(player, this.getClickType(player, packet.getAction()));
                        COOLDOWN.put(player.getUniqueId(), packet.getEntityId());
                    }
                    break;
                }
            }
        }
    }

    private ClickType getClickType(Player player, WrapperPlayClientInteractEntity.InteractAction action) {
        boolean sneak = player.isSneaking();
        ClickType clickType;
        if (action == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            clickType = sneak ? ClickType.SHIFT_LEFT_CLICK : ClickType.LEFT_CLICK;
        } else {
            clickType = sneak ? ClickType.SHIFT_RIGHT_CLICK : ClickType.RIGHT_CLICK;
        }
        return clickType;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        for (BedWarsNPCImpl value : this.factory.getNpcs().values()) {
            value.npcTracker.onMove(event);
        }
    }

}
