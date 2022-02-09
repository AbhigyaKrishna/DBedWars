package com.pepedevs.dbedwars.hologram;

import com.pepedevs.dbedwars.api.hologram.HologramSettings;
import com.pepedevs.radium.holograms.HologramManager;
import com.pepedevs.radium.task.CancellableWorkload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpdatingHologram extends HologramImpl {

    private CancellableWorkload task;

    protected UpdatingHologram(String id, Location location, HologramSettings settings) {
        super(id, location, settings);
        this.startUpdate();
    }

    @Override
    public void destroy() {
        this.stopUpdate();
        super.destroy();
    }

    public void startUpdate() {
        if (this.task != null && !this.task.isCancelled())
            throw new IllegalStateException("Update already running!");

        this.task =
                new CancellableWorkload() {
                    long lastExec = System.currentTimeMillis();

                    @Override
                    public void compute() {
                        lastExec = System.currentTimeMillis();
                        List<Player> players = new ArrayList<>();
                        for (UUID viewer : UpdatingHologram.this.viewers) {
                            Player player = Bukkit.getPlayer(viewer);
                            if (player == null) continue;
                            players.add(player);
                        }
                        UpdatingHologram.this.update(players.toArray(new Player[0]));
                    }

                    @Override
                    public boolean shouldExecute() {
                        return !this.isCancelled()
                                && System.currentTimeMillis() - this.lastExec
                                >= UpdatingHologram.this.getSettings().getUpdateInterval();
                    }
                };
        HologramManager.get().getThread().submit(task);
    }

    public void stopUpdate() {
        this.task.setCancelled(true);
    }
}

