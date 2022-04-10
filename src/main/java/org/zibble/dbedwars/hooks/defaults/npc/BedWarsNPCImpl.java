package org.zibble.dbedwars.hooks.defaults.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.hooks.npc.NPCData;
import org.zibble.dbedwars.api.util.mixin.ClickAction;
import org.zibble.dbedwars.utils.reflection.bukkit.EntityReflection;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BedWarsNPCImpl implements BedwarsNPC {

    protected static final PacketEventsAPI<?> PACKET_EVENTS_API = PacketEvents.getAPI();
    protected final Set<ClickAction> clickActions;
    protected final Set<UUID> shown;
    protected final Set<UUID> outOfRenderDistance;
    protected final NPCTracker npcTracker;
    private final Hologram hologram;
    private final String ID;
    private final NPCData npcData;
    private final UUID uuid;
    private int entityID;
    private Location location;

    public BedWarsNPCImpl(String ID, Location location, NPCData npcData) {
        this.ID = ID;
        this.npcData = npcData;
        this.location = location;
        this.entityID = EntityReflection.getFreeEntityId();
        this.uuid = new UUID(ThreadLocalRandom.current().nextLong(), 0L);
        this.hologram = DBedWarsAPI.getApi().getHookManager().getHologramFactory().createHologram(this.location.clone().add(0, 2, 0));
        this.hologram.setInverted(true);
        this.hologram.addPage();
        this.clickActions = Collections.synchronizedSet(new HashSet<>());
        this.shown = Collections.synchronizedSet(new HashSet<>());
        this.outOfRenderDistance = Collections.synchronizedSet(new HashSet<>());
        this.npcTracker = new NPCTracker(this);
        this.npcTracker.start();
    }

    @Override
    public Hologram getNameHologram() {
        return this.hologram;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public ActionFuture<BedwarsNPC> setEntityID(int id) {
        return ActionFuture.supplyAsync(() -> {
            BedWarsNPCImpl.this.entityID = id;
            return BedWarsNPCImpl.this;
        });
    }

    public String getID() {
        return this.ID;
    }

    @Override
    public ActionFuture<BedwarsNPC> spawn() {
        return ActionFuture.supplyAsync(() -> {
            if (BedWarsNPCImpl.this.location.getWorld() == null) throw new NullPointerException("World can't be null");
            for (Player player : BedWarsNPCImpl.this.location.getWorld().getPlayers()) {
                BedWarsNPCImpl.this.viewPacket(player);
                BedWarsNPCImpl.this.shown.add(player.getUniqueId());
            }
            return this;
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> teleport(Location location) {
        return ActionFuture.supplyAsync(() -> {
            if (BedWarsNPCImpl.this.location.equals(location))
                return BedWarsNPCImpl.this;

            BedWarsNPCImpl.this.location = location;
            for (UUID uuid : BedWarsNPCImpl.this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    BedWarsNPCImpl.this.changeLocationPacket(player);
                } else {
                    BedWarsNPCImpl.this.shown.remove(uuid);
                    BedWarsNPCImpl.this.outOfRenderDistance.remove(uuid);
                }
            }
            return BedWarsNPCImpl.this;
        });
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public ActionFuture<BedwarsNPC> lookAt(float yaw, float pitch) {
        byte yawByte = (byte) MathUtil.floor(yaw * 256.0F / 360.0F);
        byte pitchByte = (byte) MathUtil.floor(pitch * 256.0F / 360.0F);
        return ActionFuture.supplyAsync(() -> {
            BedWarsNPCImpl.this.location.setYaw(yaw);
            BedWarsNPCImpl.this.location.setPitch(pitch);
            for (UUID uuid : BedWarsNPCImpl.this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) BedWarsNPCImpl.this.changeDirectionPacket(player, yawByte, pitchByte);
                else {
                    BedWarsNPCImpl.this.shown.remove(uuid);
                    BedWarsNPCImpl.this.outOfRenderDistance.remove(uuid);
                }
            }
            return BedWarsNPCImpl.this;
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> lookAt(Vector direction) {
        Location a = new Location(null, 0, 1, 0);
        a.setDirection(direction);
        return this.lookAt(a.getYaw(), a.getPitch());
    }

    @Override
    public ActionFuture<BedwarsNPC> lookAt(Location location) {
        return BedWarsNPCImpl.this.lookAt(location.getYaw(), location.getPitch());
    }

    @Override
    public ActionFuture<BedwarsNPC> destroy() {
        return ActionFuture.supplyAsync(() -> {
            for (UUID uuid : BedWarsNPCImpl.this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    BedWarsNPCImpl.this.destroyPacket(player);
                }
            }
            BedWarsNPCImpl.this.shown.clear();
            BedWarsNPCImpl.this.outOfRenderDistance.clear();
            return BedWarsNPCImpl.this;
        });
    }

    @Override
    public Set<UUID> getViewers() {
        return Collections.unmodifiableSet(this.shown);
    }

    @Override
    public ActionFuture<BedwarsNPC> hide(Player player) {
        return ActionFuture.supplyAsync(() -> {
            if (!BedWarsNPCImpl.this.shown.contains(player.getUniqueId()))
                return BedWarsNPCImpl.this;

            BedWarsNPCImpl.this.destroyPacket(player);
            BedWarsNPCImpl.this.shown.remove(player.getUniqueId());
            BedWarsNPCImpl.this.outOfRenderDistance.remove(player.getUniqueId());
            return BedWarsNPCImpl.this;
        });
    }


    public ActionFuture<BedwarsNPC> silentHide(Player player) {
        return ActionFuture.supplyAsync(() -> {
            BedWarsNPCImpl.this.destroyPacket(player);
            return BedWarsNPCImpl.this;
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> show(Player player) {
        return ActionFuture.supplyAsync(() -> {
            if (BedWarsNPCImpl.this.shown.contains(player.getUniqueId()))
                return BedWarsNPCImpl.this;

            BedWarsNPCImpl.this.viewPacket(player);
            BedWarsNPCImpl.this.shown.add(player.getUniqueId());
            return BedWarsNPCImpl.this;
        });
    }


    public ActionFuture<BedwarsNPC> forceShow(Player player) {
        return ActionFuture.supplyAsync(() -> {
            BedWarsNPCImpl.this.viewPacket(player);
            return BedWarsNPCImpl.this;
        });
    }

    public ActionFuture<BedwarsNPC> addInShownList(Player player) {
        return ActionFuture.supplyAsync(() -> {
            BedWarsNPCImpl.this.shown.add(player.getUniqueId());
            return BedWarsNPCImpl.this;
        });
    }

    @Override
    public Collection<ClickAction> getClickActions() {
        return Collections.unmodifiableCollection(this.clickActions);
    }

    @Override
    public void addClickAction(ClickAction action) {
        this.clickActions.add(action);
    }

    @Override
    public NPCData getNpcData() {
        return this.npcData;
    }

    @Override
    public ActionFuture<BedwarsNPC> updateNPCData() {
        return ActionFuture.supplyAsync(() -> {
            WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(BedWarsNPCImpl.this.entityID, Collections.singletonList(new EntityData(0, EntityDataTypes.BYTE, ByteUtil.buildNPCDataByte(BedWarsNPCImpl.this.npcData))));
            for (UUID uuid : BedWarsNPCImpl.this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
            }
            return BedWarsNPCImpl.this;
        });
    }

    public UUID getUUID() {
        return uuid;
    }

    protected abstract void viewPacket(Player player);

    protected void changeLocationPacket(Player player) {
        WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport(this.getEntityID(),
                convert(this.location),
                this.location.getYaw(),
                this.location.getPitch(),
                true);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

    protected Vector3d convert(Location location) {
        return new Vector3d(location.getX(), location.getY(), location.getZ());
    }

    protected void changeDirectionPacket(Player player, byte yaw, byte pitch) {
        WrapperPlayServerEntityHeadLook yawPacket = new WrapperPlayServerEntityHeadLook(this.getEntityID(), yaw);
        WrapperPlayServerEntityRotation pitchPacket = new WrapperPlayServerEntityRotation(this.getEntityID(), yaw, pitch, true);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, yawPacket);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, pitchPacket);
    }

    protected void destroyPacket(Player player) {
        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(this.entityID);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }

}
