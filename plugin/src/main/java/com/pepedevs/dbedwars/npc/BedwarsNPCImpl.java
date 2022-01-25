package com.pepedevs.dbedwars.npc;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.npc.BedwarsNPC;
import com.pepedevs.dbedwars.api.npc.NPCBedwarsAction;
import com.pepedevs.radium.npc.EntityNPC;
import com.pepedevs.radium.npc.PlayerNPC;
import com.pepedevs.radium.npc.internal.NpcBase;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class BedwarsNPCImpl implements BedwarsNPC {

    /*private final NpcBase npc;
    private final DBedwars plugin;

    public BedwarsNPCImpl(DBedwars plugin, String id, EntityType entityType, Location location) {
        this.plugin = plugin;
        if (entityType == EntityType.PLAYER) this.npc = new PlayerNPC(id, location);
        else this.npc = new EntityNPC(id, entityType, location);
    }

    public NPCBedwarsAction spawn() {
        for (Player player : this.npc.getLocation().getWorld().getPlayers()) {
            npc.addInShownList(player);
        }
        if (npc instanceof PlayerNPC) {
            PlayerNPC playerNPC = (PlayerNPC) npc;
            playerNPC.addInTab();
            this.runLater(new Runnable() {
                @Override
                public void run() {
                    playerNPC.spawn();
                }
            }, 1);
            this.runLater(new Runnable() {
                @Override
                public void run() {
                    playerNPC.removeFromTab();
                }
            }, 3);
        } else {
            npc.spawn();
        }
        return this;
    }

    private void runLater(Runnable runnable, int ticks) {
        this.plugin.getThreadHandler().runTaskLater(runnable, ticks * 50L);
    }*/

}
