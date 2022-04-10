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
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.util.key.Keyed;
import org.zibble.dbedwars.api.util.mixin.ClickAction;
import org.zibble.dbedwars.utils.reflection.bukkit.EntityReflection;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BedWarsNPCImpl implements BedwarsNPC, Keyed {

    protected static final PacketEventsAPI<?> PACKET_EVENTS_API = PacketEvents.getAPI();
    protected final NPCFactoryImpl factory;
    protected final Set<ClickAction> clickActions;
    protected final Set<UUID> shown;
    protected final Set<UUID> outOfRenderDistance;
    protected final NPCTracker npcTracker;
    private final Hologram hologram;
    private final Key key;
    private final NPCData npcData;
    private final UUID uuid;
    private int entityID;
    private Location location;

    public BedWarsNPCImpl(NPCFactoryImpl factory, Key key, Location location) {
        this.factory = factory;
        this.key = key;
        this.npcData = new NPCDataImpl();
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
            this.entityID = id;
            return this;
        });
    }

    @Override
    public Key getKey() {
        return this.key;
    }

    @Override
    public ActionFuture<BedwarsNPC> spawn() {
        return ActionFuture.supplyAsync(() -> {
            if (this.location.getWorld() == null) throw new NullPointerException("World can't be null");
            for (Player player : this.location.getWorld().getPlayers()) {
                this.viewPacket(player);
                this.shown.add(player.getUniqueId());
            }
            return this;
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> teleport(Location location) {
        return ActionFuture.supplyAsync(() -> {
            if (this.location.equals(location))
                return this;

            this.location = location;
            for (UUID uuid : this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    this.changeLocationPacket(player);
                } else {
                    this.shown.remove(uuid);
                    this.outOfRenderDistance.remove(uuid);
                }
            }
            return this;
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
            this.location.setYaw(yaw);
            this.location.setPitch(pitch);
            for (UUID uuid : this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) this.changeDirectionPacket(player, yawByte, pitchByte);
                else {
                    this.shown.remove(uuid);
                    this.outOfRenderDistance.remove(uuid);
                }
            }
            return this;
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
        return this.lookAt(location.getYaw(), location.getPitch());
    }

    @Override
    public ActionFuture<BedwarsNPC> destroy() {
        return ActionFuture.supplyAsync(() -> {
            for (UUID uuid : this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    this.destroyPacket(player);
                }
            }
            this.shown.clear();
            this.outOfRenderDistance.clear();
            this.npcTracker.stop();
            this.factory.getNpcs().remove(this.key);
            return this;
        });
    }

    @Override
    public Set<UUID> getViewers() {
        return Collections.unmodifiableSet(this.shown);
    }

    @Override
    public ActionFuture<BedwarsNPC> hide(Player player) {
        return ActionFuture.supplyAsync(() -> {
            if (!this.shown.contains(player.getUniqueId()))
                return this;

            this.destroyPacket(player);
            this.shown.remove(player.getUniqueId());
            this.outOfRenderDistance.remove(player.getUniqueId());
            return this;
        });
    }


    public ActionFuture<BedwarsNPC> silentHide(Player player) {
        return ActionFuture.supplyAsync(() -> {
            this.destroyPacket(player);
            return this;
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> show(Player player) {
        return ActionFuture.supplyAsync(() -> {
            if (this.shown.contains(player.getUniqueId()))
                return this;

            this.viewPacket(player);
            this.shown.add(player.getUniqueId());
            return this;
        });
    }


    public ActionFuture<BedwarsNPC> forceShow(Player player) {
        return ActionFuture.supplyAsync(() -> {
            this.viewPacket(player);
            return this;
        });
    }

    public ActionFuture<BedwarsNPC> addInShownList(Player player) {
        return ActionFuture.supplyAsync(() -> {
            this.shown.add(player.getUniqueId());
            return this;
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
            WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(this.entityID, Collections.singletonList(new EntityData(0, EntityDataTypes.BYTE, ByteUtil.buildNPCDataByte(this.npcData))));
            for (UUID uuid : this.shown) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null) continue;
                PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
            }
            return this;
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
