package org.zibble.dbedwars.hooks.defaults.hologram;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.utils.reflection.bukkit.EntityReflection;

import java.util.*;

public final class PacketUtils {

    private static final PacketEventsAPI<?> PACKET_EVENTS_API = PacketEvents.getAPI();

    private PacketUtils() {
    }

    public static int getFreeEntityId() {
        return EntityReflection.getFreeEntityId();
    }

    public static void showFakeEntity(@NotNull Player player, @NotNull Location location, @NotNull EntityType entityType, int entityId) {
        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(entityId,
                Optional.of(UUID.randomUUID()),
                entityType,
                SpigotConversionUtil.fromBukkitLocation(location).getPosition(),
                location.getPitch(),
                location.getYaw(),
                0,
                0,
                Optional.empty());
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void showFakeEntityLiving(@NotNull Player player, @NotNull Location location, @NotNull EntityType entityType, int entityId) {
        EntityData entityData = new EntityData(15, EntityDataTypes.BYTE, (byte) 0);
        WrapperPlayServerSpawnLivingEntity packet = new WrapperPlayServerSpawnLivingEntity(
                entityId,
                UUID.randomUUID(),
                entityType,
                SpigotConversionUtil.fromBukkitLocation(location).getPosition(),
                location.getYaw(),
                location.getPitch(),
                location.getYaw(),
                Vector3d.zero(),
                Collections.singletonList(entityData)
        );
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void showFakeEntityLiving(Player player, Location location, EntityType entityType, int entityId, List<EntityData> entityDatas) {
        WrapperPlayServerSpawnLivingEntity packet = new WrapperPlayServerSpawnLivingEntity(
                entityId,
                UUID.randomUUID(),
                entityType,
                SpigotConversionUtil.fromBukkitLocation(location).getPosition(),
                location.getYaw(),
                location.getPitch(),
                location.getYaw(),
                Vector3d.zero(),
                entityDatas);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void showFakeEntityArmorStand(Player player, Location location, int entityId, boolean invisible, boolean small, boolean clickable) {
        EntityData invisData = new EntityData(0, EntityDataTypes.BYTE, (byte) (invisible ? 0x20 : 0x00));
        byte b = 0x08;
        if (small) b |= 0x01;
        if (!clickable) b |= 0x10;
        EntityData data = new EntityData(10, EntityDataTypes.BYTE, b);
        PacketUtils.showFakeEntityLiving(player, location, EntityTypes.ARMOR_STAND, entityId, Arrays.asList(invisData, data));
    }

    public static void showFakeEntityItem(@NotNull Player player, @NotNull Location location, @NotNull ItemStack itemStack, int entityId) {
        PacketUtils.showFakeEntity(player, location, EntityTypes.ITEM, entityId);

        EntityData entityData = new EntityData(10, EntityDataTypes.ITEMSTACK, DBedwars.getInstance().getNMSAdaptor().asPacketItem(itemStack));
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityId, Collections.singletonList(entityData));
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);

        PacketUtils.teleportFakeEntity(player, location, entityId);
    }

    public static void updateFakeEntityCustomName(@NotNull Player player, @NotNull Component name, int entityId) {
        EntityData customName;
        String legacyName = AdventureUtils.toVanillaString(name); // TODO: 11-04-2022 support latest version
        customName = new EntityData(2, EntityDataTypes.STRING, legacyName);
        EntityData customNameVisible = new EntityData(3, EntityDataTypes.BYTE, (byte) 1);
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityId, Arrays.asList(customName, customNameVisible));
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void teleportFakeEntity(@NotNull Player player, @NotNull Location location, int entityId) {
        WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport(entityId, SpigotConversionUtil.fromBukkitLocation(location).getPosition(), location.getYaw(), location.getPitch(), true);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void helmetFakeEntity(@NotNull Player player, @NotNull ItemStack itemStack, int entityId) {
        Equipment equipment = new Equipment(EquipmentSlot.HELMET, DBedwars.getInstance().getNMSAdaptor().asPacketItem(itemStack));
        WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment(entityId, Collections.singletonList(equipment));
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void attachFakeEntity(@NotNull Player player, int vehicleId, int entityId) {
        WrapperPlayServerAttachEntity packet = new WrapperPlayServerAttachEntity(entityId, vehicleId, false);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void hideFakeEntities(@NotNull Player player, int... entityIds) {
        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(entityIds);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

}
