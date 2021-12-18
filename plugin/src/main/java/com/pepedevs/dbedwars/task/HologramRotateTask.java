package com.pepedevs.dbedwars.task;

import com.pepedevs.corelib.holograms.object.Hologram;
import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.task.CancellableTask;
import org.bukkit.Location;

public abstract class HologramRotateTask {

    protected final DBedwars plugin;
    protected final Hologram hologram;
    protected CancellableTask task;

    public HologramRotateTask(DBedwars plugin, Hologram hologram) {
        this.plugin = plugin;
        this.hologram = hologram;
    }

    public abstract void start();

    public void cancel() {
        if (this.task == null) throw new IllegalStateException("Task already stopped!");

        this.task.setCancelled(true);
        this.task = null;
    }

    protected void rotateAndMoveHologram(double x, double y, double z, float yaw, float pitch) {
        Location location = hologram.getLocation().clone();
        location.setX(location.getX() + x);
        location.setY(location.getY() + y);
        location.setZ(location.getZ() + z);
        location.setYaw(location.getYaw() + yaw);
        location.setPitch(location.getPitch() + pitch);
//        this.hologram.teleport(location);
        // TODO
    }

    protected void rotateHologram(float yaw, float pitch) {
        Location location = hologram.getLocation().clone();
        location.setYaw(location.getYaw() + yaw);
        location.setPitch(location.getPitch() + pitch);
//        this.hologram.teleport(location);
    }

    public Hologram getHologram() {
        return this.hologram;
    }

}
