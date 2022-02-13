package com.pepedevs.dbedwars.api.npc;

import com.pepedevs.dbedwars.api.future.ActionFuture;
import com.pepedevs.radium.npc.action.NPCClickAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface BedwarsNPC {

    ActionFuture<BedwarsNPC> spawn();

    ActionFuture<BedwarsNPC> teleport(Location location);

    Location getLocation();

    ActionFuture<BedwarsNPC> lookAt(float yaw, float pitch);

    ActionFuture<BedwarsNPC> lookAt(Vector direction);

    ActionFuture<BedwarsNPC> lookAt(Location location);

    ActionFuture<BedwarsNPC> destroy();

    Set<UUID> getViewers();

    ActionFuture<BedwarsNPC> hide(Player player);

    ActionFuture<BedwarsNPC> show(Player player);

    Collection<NPCClickAction> getClickActions();

    void addClickAction(NPCClickAction action);

    NPCData getNpcData();

    default ActionFuture<BedwarsNPC> setCrouching(boolean crouching) {
        this.getNpcData().setCrouched(crouching);
        return this.updateNPCData();
    }

    default boolean isCrouching() {
        return this.getNpcData().isCrouched();
    }

    default ActionFuture<BedwarsNPC> setOnFire(boolean onFire) {
        this.getNpcData().setOnFire(onFire);
        return this.updateNPCData();
    }

    default boolean isOnFire() {
        return this.getNpcData().isOnFire();
    }

    ActionFuture<BedwarsNPC> updateNPCData();

}
