package com.pepedevs.dbedwars.api.npc;

import com.pepedevs.dbedwars.api.util.BedwarsCompletable;
import com.pepedevs.radium.npc.action.NPCClickAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public abstract class BedwarsNPC {

    private final String ID;

    public BedwarsNPC(String ID) {
        this.ID = ID;
    }

    public abstract int getEntityId();

    public abstract BedwarsCompletable<BedwarsNPC> setEntityId(int id);

    public String getId() {
        return this.ID;
    }

    public abstract BedwarsCompletable<BedwarsNPC> spawn();

    public abstract BedwarsCompletable<BedwarsNPC> teleport(Location location);

    public abstract Location getLocation();

    public abstract BedwarsCompletable<BedwarsNPC> look(float yaw, float pitch);

    public abstract BedwarsCompletable<BedwarsNPC> lookAt(Vector direction);

    public abstract BedwarsCompletable<BedwarsNPC> lookAt(Location location);

    public abstract BedwarsCompletable<BedwarsNPC> destroy();

    public abstract Set<UUID> getViewers();

    public abstract BedwarsCompletable<BedwarsNPC> hide(Player player);

    public abstract BedwarsCompletable<BedwarsNPC> silentHide(Player player);

    public abstract BedwarsCompletable<BedwarsNPC> show(Player player);

    public abstract BedwarsCompletable<BedwarsNPC> forceShow(Player player);

    public abstract BedwarsCompletable<BedwarsNPC> addInShownList(Player player);

    public abstract Collection<NPCClickAction> getClickActions();

    public abstract void addClickAction(NPCClickAction action);

    public abstract NPCData getNpcData();

    public void setCrouching(boolean crouching) {
        this.getNpcData().setCrouched(crouching);
        this.updateNPCData();
    }

    public boolean isCrouching() {
        return this.getNpcData().isCrouched();
    }

    public void setOnFire(boolean onFire) {
        this.getNpcData().setOnFire(onFire);
        this.updateNPCData();
    }

    public boolean isOnFire() {
        return this.getNpcData().isOnFire();
    }

    protected abstract void updateNPCData();

}
