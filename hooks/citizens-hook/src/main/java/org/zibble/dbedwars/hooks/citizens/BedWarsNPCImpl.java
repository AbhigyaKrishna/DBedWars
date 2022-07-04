package org.zibble.dbedwars.hooks.citizens;

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
import org.zibble.dbedwars.api.util.mixin.ClickAction;

import java.util.*;

public abstract class BedWarsNPCImpl implements BedwarsNPC {

    //TODO INIT
    private final CitizensHook hook;
    private final NPCData npcData;
    private final Set<ClickAction> clickActions;
    private Hologram hologram;
    private Location location;
    private NPC citizensNPC;

    public BedWarsNPCImpl(CitizensHook hook, Location location) {
        this.hook = hook;
        this.location = location;
        this.npcData = new NPCData();
        this.clickActions = new HashSet<>();
        this.citizensNPC = this.createNPC();
    }

    @Override
    public Hologram getNameHologram() {
        return hologram;
    }

    @Override
    public ActionFuture<BedwarsNPC> spawn() {
        this.hook.getNpcs().add(this);
        return ActionFuture.supplyAsync(() -> {
            if (this.citizensNPC == null) this.citizensNPC = this.createNPC();
            this.citizensNPC.spawn(location, SpawnReason.PLUGIN);
            return this;
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> teleport(Location location) {
        return ActionFuture.supplyAsync(() -> {
            this.location = location;
            this.citizensNPC.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            return this;
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
        return ActionFuture.supplyAsync(() -> {
            this.location.setDirection((location.toVector().subtract(this.location.toVector())));
            this.citizensNPC.faceLocation(location);
            return this;
        });
    }

    @Override
    public ActionFuture<BedwarsNPC> destroy() {
        return ActionFuture.supplyAsync(() -> {
            this.citizensNPC.destroy();
            this.citizensNPC = null;
            this.hook.getNpcs().remove(this);
            return this;
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
    public Collection<ClickAction> getClickActions() {
        return Collections.unmodifiableSet(this.clickActions);
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
            if (this.npcData.isCrouched()) {
                this.citizensNPC.getOrAddTrait(CitizensAPI.getTraitFactory().getTrait("sneak").getClass());
            } else {
                this.citizensNPC.removeTrait(CitizensAPI.getTraitFactory().getTrait("sneak").getClass());
            }

            if (this.npcData.isOnFire()) {
                this.citizensNPC.getEntity().setFireTicks(Integer.MAX_VALUE);
            } else {
                this.citizensNPC.getEntity().setFireTicks(0);
            }
            return this;
        });
    }

    public abstract NPC createNPC();

    protected NPC getCitizensNPC() {
        return this.citizensNPC;
    }

}
