package org.zibble.dbedwars.hooks.citizens.npc;

import com.pepedevs.radium.npc.action.NPCClickAction;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.hooks.npc.NPCData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class BedwarsNPCImpl implements BedwarsNPC {

    //TODO INIT
    private Hologram hologram;
    private Location location;
    private final NPCData npcData;

    private NPC citizensNPC;

    public BedwarsNPCImpl(Location location, NPCData npcData) {
        this.location = location;
        this.npcData = npcData;
        this.citizensNPC = this.createNPC();
    }

    @Override
    public Hologram getNameHologram() {
        return hologram;
    }

    @Override
    public ActionFuture<BedwarsNPC> spawn() {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                if (BedwarsNPCImpl.this.citizensNPC == null) BedwarsNPCImpl.this.citizensNPC = BedwarsNPCImpl.this.createNPC();
                BedwarsNPCImpl.this.citizensNPC.spawn(location, SpawnReason.PLUGIN);
                return BedwarsNPCImpl.this;
            }
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> teleport(Location location) {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                BedwarsNPCImpl.this.location = location;
                BedwarsNPCImpl.this.citizensNPC.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
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
        Location a = this.location.clone();
        a.setYaw(yaw);
        a.setPitch(pitch);
        a.add(a.getDirection().normalize());
        return lookAt(a);
    }

    @Override
    public ActionFuture<BedwarsNPC> lookAt(Vector direction) {
        return lookAt(this.location.clone().add(direction.normalize()));
    }

    @Override
    public ActionFuture<BedwarsNPC> lookAt(Location location) {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                BedwarsNPCImpl.this.location.setDirection((location.toVector().subtract(BedwarsNPCImpl.this.location.toVector())));
                BedwarsNPCImpl.this.citizensNPC.faceLocation(location);
                return BedwarsNPCImpl.this;
            }
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> destroy() {
        return ActionFuture.supplyAsync(new Supplier<BedwarsNPC>() {
            @Override
            public BedwarsNPC get() {
                BedwarsNPCImpl.this.citizensNPC.destroy();
                BedwarsNPCImpl.this.citizensNPC = null;
                return BedwarsNPCImpl.this;
            }
        });
    }

    @Override
    public Set<UUID> getViewers() {
        Set<UUID> returnVal = new HashSet<>();
        for (Player player : this.location.getWorld().getPlayers()) {
            returnVal.add(player.getUniqueId());
        }
        return returnVal;
    }

    @Override
    public ActionFuture<BedwarsNPC> hide(Player player) {
        throw new UnsupportedOperationException("This feature is not yet supported in Citizens Hook.");
    }

    @Override
    public ActionFuture<BedwarsNPC> show(Player player) {
        throw new UnsupportedOperationException("This feature is not yet supported in Citizens Hook.");
    }

    @Override
    public Collection<NPCClickAction> getClickActions() {
        //TODO
        return null;
    }

    @Override
    public void addClickAction(NPCClickAction action) {
        //TODO
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
                if (BedwarsNPCImpl.this.npcData.isCrouched()) {
                    BedwarsNPCImpl.this.citizensNPC.getOrAddTrait(CitizensAPI.getTraitFactory().getTrait("sneak").getClass());
                } else {
                    BedwarsNPCImpl.this.citizensNPC.removeTrait(CitizensAPI.getTraitFactory().getTrait("sneak").getClass());
                }

                if (BedwarsNPCImpl.this.npcData.isOnFire()) {
                    BedwarsNPCImpl.this.citizensNPC.getEntity().setFireTicks(Integer.MAX_VALUE);
                } else {
                    BedwarsNPCImpl.this.citizensNPC.getEntity().setFireTicks(0);
                }
                return BedwarsNPCImpl.this;
            }
        });
    }

    public abstract NPC createNPC();

    protected NPC getCitizensNPC() {
        return this.citizensNPC;
    }

}
