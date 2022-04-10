package org.zibble.dbedwars.api.objects.hologram;

import org.bukkit.Location;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.task.CancellableWorkload;

public abstract class HologramRotateTask {

    protected final Hologram hologram;
    protected final TaskEndAction endAction;
    protected CancellableWorkload task;

    public HologramRotateTask(Hologram hologram, TaskEndAction endAction) {
        this.hologram = hologram;
        this.endAction = endAction;
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
        this.hologram.teleport(location);
    }

    protected void rotateHologram(float yaw, float pitch) {
        Location location = hologram.getLocation().clone();
        location.setYaw(location.getYaw() + yaw);
        location.setPitch(location.getPitch() + pitch);
        this.hologram.teleport(location);
    }

    public Hologram getHologram() {
        return this.hologram;
    }

    public enum TaskEndAction {
        REVERSE,
        REPEAT,
        ;
    }

}
