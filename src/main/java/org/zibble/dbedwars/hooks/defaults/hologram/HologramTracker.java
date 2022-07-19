package org.zibble.dbedwars.hooks.defaults.hologram;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.zibble.dbedwars.api.task.CancellableWorkload;

import java.util.UUID;

public class HologramTracker {

    private final HologramImpl hologram;
    private CancellableWorkload task;

    public HologramTracker(HologramImpl hologram) {
        this.hologram = hologram;
    }

    public void start() {
        this.task = new CancellableWorkload() {

            private long lastRun = System.currentTimeMillis();

            @Override
            public void compute() {
                if (this.isCancelled()) return;
                this.lastRun = System.currentTimeMillis();
                HologramTracker.this.hologram.outOfRenderDistance.removeIf(uuid -> !HologramTracker.this.hologram.viewers.contains(uuid));
                for (UUID uuid : HologramTracker.this.hologram.viewers) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) continue;
                    if (!HologramTracker.this.canSee(player)) HologramTracker.this.hologram.outOfRenderDistance.add(uuid);
                }
            }

            @Override
            public boolean shouldExecute() {
                return !this.isCancelled() && System.currentTimeMillis() - lastRun >= 100;
            }
        };
        this.hologram.manager.thread.submit(this.task);
    }

    public void stop() {
        this.task.setCancelled(true);
        this.task = null;
    }

    public void onMove(PlayerMoveEvent event) {
        if (this.task == null) return;
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (!hologram.viewers.contains(player.getUniqueId()) || !hologram.outOfRenderDistance.contains(player.getUniqueId()))
            return;
        if (!this.canSee(player)) return;
        hologram.show(player);
        hologram.outOfRenderDistance.remove(player.getUniqueId());
    }

    private boolean canSee(Player player) {
        Vector vector = player.getLocation().toVector().subtract(hologram.getLocation().toVector());
        return vector.getX() >= -hologram.getDisplayRange() &&
                vector.getX() <= hologram.getDisplayRange() &&
                vector.getZ() >= -hologram.getDisplayRange() &&
                vector.getZ() <= hologram.getDisplayRange();
    }

}
