package org.zibble.dbedwars.hooks.defaults.npc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.task.Workload;

import java.util.UUID;

public class NPCTracker {

    private static final DBedwars PLUGIN = DBedwars.getInstance();

    private final BedWarsNPCImpl npc;

    public NPCTracker(BedWarsNPCImpl npc) {
        this.npc = npc;
    }

    public void start() {
        PLUGIN.getThreadHandler().submitAsync(new Workload() {

            private long lastRun = System.currentTimeMillis();

            @Override
            public void compute() {
                this.lastRun = System.currentTimeMillis();
                NPCTracker.this.npc.outOfRenderDistance.removeIf(uuid -> !NPCTracker.this.npc.shown.contains(uuid));
                for (UUID uuid : NPCTracker.this.npc.shown) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) continue;
                    if (!NPCTracker.this.canSee(player)) NPCTracker.this.npc.outOfRenderDistance.add(uuid);
                }
            }

            @Override
            public boolean shouldExecute() {
                return System.currentTimeMillis() - lastRun >= 100;
            }

            @Override
            public boolean reSchedule() {
                return true;
            }
        });
    }

    public void onMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (!npc.shown.contains(player.getUniqueId()) || !npc.outOfRenderDistance.contains(player.getUniqueId())) return;
        if (!this.canSee(player)) return;
        npc.forceShow(player);
        npc.outOfRenderDistance.remove(player.getUniqueId());
    }

    private boolean canSee(Player player) {
        Vector vector = player.getLocation().toVector().subtract(npc.getLocation().toVector());
        int a = Math.min(Math.min(256, (Bukkit.getViewDistance() - 1) * 16), (player.getClientViewDistance() - 1) * 16);
        return
                vector.getX() >= -a &&
                        vector.getX() <= a &&
                        vector.getZ() >= -a &&
                        vector.getZ() <= a;
    }

}
