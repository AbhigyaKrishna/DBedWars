package com.pepedevs.dbedwars.api.npc;

import com.pepedevs.dbedwars.api.util.BedwarsCompletable;
import com.pepedevs.radium.npc.action.NPCClickAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface BedwarsNPC {

    int getEntityId();

    BedwarsCompletable<BedwarsNPC> setEntityId(int id);

    String getID();

    BedwarsCompletable<BedwarsNPC> spawn();

    BedwarsCompletable<BedwarsNPC> teleport(Location location);

    Location getLocation();

    BedwarsCompletable<BedwarsNPC> look(float yaw, float pitch);

    BedwarsCompletable<BedwarsNPC> lookAt(Vector direction);

    BedwarsCompletable<BedwarsNPC> lookAt(Location location);

    BedwarsCompletable<BedwarsNPC> destroy();

    Set<UUID> getViewers();

    BedwarsCompletable<BedwarsNPC> hide(Player player);

    BedwarsCompletable<BedwarsNPC> silentHide(Player player);

    BedwarsCompletable<BedwarsNPC> show(Player player);

    BedwarsCompletable<BedwarsNPC> forceShow(Player player);

    BedwarsCompletable<BedwarsNPC> addInShownList(Player player);

    Collection<NPCClickAction> getClickActions();

    void addClickAction(NPCClickAction action);

    NPCData getNpcData();

    default void setCrouching(boolean crouching) {
        this.getNpcData().setCrouched(crouching);
        this.updateNPCData();
    }

    default boolean isCrouching() {
        return this.getNpcData().isCrouched();
    }

    default void setOnFire(boolean onFire) {
        this.getNpcData().setOnFire(onFire);
        this.updateNPCData();
    }

    default boolean isOnFire() {
        return this.getNpcData().isOnFire();
    }

    void updateNPCData();

}
