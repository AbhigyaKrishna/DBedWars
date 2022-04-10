package org.zibble.dbedwars.api.hooks.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.util.mixin.ClickAction;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface BedwarsNPC {

    Hologram getNameHologram();

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

    Collection<ClickAction> getClickActions();

    void addClickAction(ClickAction action);

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
