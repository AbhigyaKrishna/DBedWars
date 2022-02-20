package org.zibble.dbedwars.api.objects.serializable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocationXYZ implements Cloneable {

    private double x = 0.0D;
    private double y = 0.0D;
    private double z = 0.0D;

    public LocationXYZ() {}

    public LocationXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static LocationXYZ valueOf(Location loc) {
        return new LocationXYZ(loc.getX(), loc.getY(), loc.getZ());
    }

    public static LocationXYZ valueOf(Vector vector) {
        return new LocationXYZ(vector.getX(), vector.getY(), vector.getZ());
    }

    // Format: x, y, z
    public static LocationXYZ valueOf(String s) {
        String[] arr = s.split(",");
        if (arr.length != 3) return null;

        for (String i : arr) {
            try {
                Double.parseDouble(i.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return new LocationXYZ(
                Double.parseDouble(arr[0]), Double.parseDouble(arr[1]), Double.parseDouble(arr[2]));
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public LocationXYZ getXYZ() {
        return this;
    }

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LocationXYZ addX(double x) {
        this.x += x;
        return this;
    }

    public LocationXYZ addY(double y) {
        this.y += y;
        return this;
    }

    public LocationXYZ addZ(double z) {
        this.z += z;
        return this;
    }

    public LocationXYZ subtractX(double x) {
        this.x -= x;
        return this;
    }

    public LocationXYZ subtractY(double y) {
        this.y -= y;
        return this;
    }

    public LocationXYZ subtractZ(double z) {
        this.z -= z;
        return this;
    }

    public Block getBlock(World world) {
        return world.getBlockAt((int) this.x, (int) this.y, (int) this.z);
    }

    public Location toBukkit(World world) {
        return new Location(world, this.x, this.y, this.z);
    }

    public void teleport(World world, Player player) {
        player.teleport(this.toBukkit(world));
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return String.valueOf(this.x + ", " + this.y + ", " + this.z);
    }

    @Override
    public LocationXYZ clone() {
        try {
            LocationXYZ clone = (LocationXYZ) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
