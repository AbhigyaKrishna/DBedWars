package org.zibble.dbedwars.hooks.defaults.hologram;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import com.pepedevs.radium.adventure.AdventureUtils;
import com.pepedevs.radium.utils.reflection.bukkit.EntityReflection;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class PacketUtils {

    private static final PacketEventsAPI<?> PACKET_EVENTS_API = PacketEvents.getAPI();

    private PacketUtils() {
    }

    public static int getFreeEntityId() {
        return EntityReflection.getFreeEntityId();
    }

    public static void showFakeEntity(Player player, Location location, EntityType entityType, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        WrapperPlayServerSpawnEntity packet = new WrapperPlayServerSpawnEntity(entityId,
                Optional.of(UUID.randomUUID()),
                entityType,
                convert(location),
                location.getPitch(),
                location.getYaw(),
                0,
                Optional.empty());
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void showFakeEntityLiving(Player player, Location location, EntityType entityType, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        EntityData entityData = new EntityData(15, EntityDataTypes.BYTE, (byte) 0);
        WrapperPlayServerSpawnLivingEntity packet = new WrapperPlayServerSpawnLivingEntity(
                entityId,
                UUID.randomUUID(),
                entityType,
                convert(location),
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
                convert(location),
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

    public static void showFakeEntityItem(Player player, Location location, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);
        Validate.notNull(itemStack);

        PacketUtils.showFakeEntity(player, location, EntityTypes.ITEM, entityId);

        EntityData entityData = new EntityData(10, EntityDataTypes.ITEMSTACK, convert(itemStack));
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityId, Collections.singletonList(entityData));
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);

        PacketUtils.teleportFakeEntity(player, location, entityId);
    }

    public static void updateFakeEntityCustomName(Player player, Component name, int entityId) {
        Validate.notNull(player);
        Validate.notNull(name);
        String legacyName = AdventureUtils.toVanillaString(name);
        EntityData customName = new EntityData(2, EntityDataTypes.STRING, legacyName);
        EntityData customNameVisible = new EntityData(3, EntityDataTypes.BYTE, legacyName);
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(entityId, Arrays.asList(customName, customNameVisible));
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void teleportFakeEntity(Player player, Location location, int entityId) {
        Validate.notNull(player);
        Validate.notNull(location);

        WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport(entityId, convert(location), location.getYaw(), location.getPitch(), true);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void helmetFakeEntity(Player player, ItemStack itemStack, int entityId) {
        Validate.notNull(player);
        Validate.notNull(itemStack);

        Equipment equipment = new Equipment(EquipmentSlot.HELMET, convert(itemStack));
        WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment(entityId, equipment);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void attachFakeEntity(Player player, int vehicleId, int entityId) {
        Validate.notNull(player);

        WrapperPlayServerAttachEntity packet = new WrapperPlayServerAttachEntity(entityId, vehicleId, false);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    public static void hideFakeEntities(Player player, int... entityIds) {
        Validate.notNull(player);

        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(entityIds);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    private static Vector3d convert(Location location) {
        return new Vector3d(location.getX(), location.getY(), location.getZ());
    }

    private static com.github.retrooper.packetevents.protocol.item.ItemStack convert(ItemStack itemStack) {
        List<Enchantment> enchantments = new ArrayList<>();
        for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
            enchantments.add(Enchantment.builder().type(EnchantmentTypes.getByName(entry.getKey().getName()))
                    .level(entry.getValue()).build());
        }
        com.github.retrooper.packetevents.protocol.item.ItemStack.Builder builder = com.github.retrooper.packetevents.protocol.item.ItemStack.builder()
                .type(ItemTypes.getByName(itemStack.getType().name()))
                .amount(itemStack.getAmount());
        for (Enchantment enchantment : enchantments) {
            builder.addEnchantment(enchantment);
        }
        builder.legacyData(itemStack.getData().getData());
        return builder.build();
    }

}
