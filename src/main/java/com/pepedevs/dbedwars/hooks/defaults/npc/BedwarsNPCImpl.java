package com.pepedevs.dbedwars.hooks.defaults.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import com.pepedevs.dbedwars.api.future.ActionFuture;
import com.pepedevs.dbedwars.api.hooks.hologram.Hologram;
import com.pepedevs.dbedwars.api.hooks.npc.BedwarsNPC;
import com.pepedevs.dbedwars.api.hooks.npc.NPCData;
import com.pepedevs.dbedwars.hooks.defaults.hologram.HologramImpl;
import com.pepedevs.radium.npc.action.NPCClickAction;
import com.pepedevs.radium.utils.reflection.bukkit.EntityReflection;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public abstract class BedwarsNPCImpl implements BedwarsNPC {

    protected static final PacketEventsAPI<?> PACKET_EVENTS_API = PacketEvents.getAPI();

    //TODO INIT
    private Hologram hologram;
    private final String ID;
    private final NPCData npcData;

    private int entityID;
    private final UUID uuid;
    private Location location;

    protected Set<NPCClickAction> clickActions = Collections.synchronizedSet(new HashSet<>());
    protected Set<UUID> shown = Collections.synchronizedSet(new HashSet<>());

    public BedwarsNPCImpl(String ID, Location location, NPCData npcData, Component name) {
        this.ID = ID;
        this.npcData = npcData;
        this.location = location;
        this.entityID = EntityReflection.getFreeEntityId();
        this.uuid = new UUID(ThreadLocalRandom.current().nextLong(), 0L);
        this.hologram = new HologramImpl(this.location.clone().add(0, 1, 0));
        this.hologram.setInverted(true);
        this.hologram.addPage().addNewLine(name, 0.5F);
    }


    @Override
    public Hologram getNameHologram() {
        return this.hologram;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public ActionFuture<BedwarsNPC> setEntityID(int id) {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                BedwarsNPCImpl.this.entityID = id;
                return BedwarsNPCImpl.this;
            }
        });
    }

    public String getID() {
        return this.ID;
    }

    @Override
    public ActionFuture<BedwarsNPC> spawn() {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                if (BedwarsNPCImpl.this.location.getWorld() == null) throw new NullPointerException("World can't be null");
                for (Player player : BedwarsNPCImpl.this.location.getWorld().getPlayers()) {
                    BedwarsNPCImpl.this.viewPacket(player);
                    BedwarsNPCImpl.this.shown.add(player.getUniqueId());
                }
                return BedwarsNPCImpl.this;
            }
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> teleport(Location location) {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                if (BedwarsNPCImpl.this.location.equals(location))
                    return BedwarsNPCImpl.this;

                BedwarsNPCImpl.this.location = location;
                for (UUID uuid : BedwarsNPCImpl.this.shown) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        BedwarsNPCImpl.this.changeLocationPacket(player);
                    } else {
                        BedwarsNPCImpl.this.shown.remove(uuid);
                    }
                }
                return BedwarsNPCImpl.this;
            }
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
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                BedwarsNPCImpl.this.location.setYaw(yaw);
                BedwarsNPCImpl.this.location.setPitch(pitch);
                for (UUID uuid : BedwarsNPCImpl.this.shown) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) BedwarsNPCImpl.this.changeDirectionPacket(player, yawByte, pitchByte);
                    else BedwarsNPCImpl.this.shown.remove(uuid);
                }
                return BedwarsNPCImpl.this;
            }
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
        return BedwarsNPCImpl.this.lookAt(location.getYaw(), location.getPitch());
    }

    @Override
    public ActionFuture<BedwarsNPC> destroy() {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                for (UUID uuid : BedwarsNPCImpl.this.shown) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        BedwarsNPCImpl.this.destroyPacket(player);
                    }
                }
                BedwarsNPCImpl.this.shown.clear();
                return BedwarsNPCImpl.this;
            }
        });
    }

    @Override
    public Set<UUID> getViewers() {
        return Collections.unmodifiableSet(this.shown);
    }

    @Override
    public ActionFuture<BedwarsNPC> hide(Player player) {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                if (!BedwarsNPCImpl.this.shown.contains(player.getUniqueId()))
                    return BedwarsNPCImpl.this;

                BedwarsNPCImpl.this.destroyPacket(player);
                BedwarsNPCImpl.this.shown.remove(player.getUniqueId());
                return BedwarsNPCImpl.this;
            }
        });
    }


    public ActionFuture<BedwarsNPC> silentHide(Player player) {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                BedwarsNPCImpl.this.destroyPacket(player);
                return BedwarsNPCImpl.this;
            }
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> show(Player player) {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                if (BedwarsNPCImpl.this.shown.contains(player.getUniqueId()))
                    return BedwarsNPCImpl.this;

                BedwarsNPCImpl.this.viewPacket(player);
                BedwarsNPCImpl.this.shown.add(player.getUniqueId());
                return BedwarsNPCImpl.this;
            }
        });
    }


    public ActionFuture<BedwarsNPC> forceShow(Player player) {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                BedwarsNPCImpl.this.viewPacket(player);
                return BedwarsNPCImpl.this;
            }
        });
    }

    public ActionFuture<BedwarsNPC> addInShownList(Player player) {
        return null;
    }

    @Override
    public Collection<NPCClickAction> getClickActions() {
        return Collections.unmodifiableCollection(this.clickActions);
    }

    @Override
    public void addClickAction(NPCClickAction action) {
        this.clickActions.add(action);
    }

    @Override
    public NPCData getNpcData() {
        return this.npcData;
    }

    @Override
    public ActionFuture<BedwarsNPC> updateNPCData() {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(BedwarsNPCImpl.this.entityID, Collections.singletonList(new EntityData(0, EntityDataTypes.BYTE, ByteUtil.buildNPCDataByte(BedwarsNPCImpl.this.npcData))));
                for (UUID uuid : BedwarsNPCImpl.this.shown) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) continue;
                    PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
                }
                return BedwarsNPCImpl.this;
            }
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
        WrapperPlayServerEntityRotation pitchPacket = new WrapperPlayServerEntityRotation(this.getEntityID(), yaw, pitch,true);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, yawPacket);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, pitchPacket);
    }

    protected void destroyPacket(Player player) {
        WrapperPlayServerDestroyEntities packet = new WrapperPlayServerDestroyEntities(this.entityID);
        PACKET_EVENTS_API.getPlayerManager().sendPacket(player, packet);
    }
}
