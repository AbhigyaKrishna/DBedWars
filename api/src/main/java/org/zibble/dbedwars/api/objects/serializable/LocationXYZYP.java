package org.zibble.dbedwars.api.objects.serializable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationXYZYP {

    private static final Pattern PATTERN = Pattern.compile("^(?<x>[+-]?\\d*\\.?\\d*)::(?<y>[+-]?\\d*\\.?\\d*)::(?<z>[+-]?\\d*\\.?\\d*)(?:::(?<yaw>[+-]?\\d*\\.?\\d*)(?:::(?<pitch>[+-]?\\d*\\.?\\d*))?)?$", Pattern.CASE_INSENSITIVE);

    private double x = 0.0D;
    private double y = 0.0D;
    private double z = 0.0D;
    private float yaw = 0.0F;
    private float pitch = 0.0F;

    public static LocationXYZYP of(double x, double y, double z) {
        return of(x, y, z, 0.0F, 0.0F);
    }

    public static LocationXYZYP of(double x, double y, double z, float yaw, float pitch) {
        return new LocationXYZYP(x, y, z, yaw, pitch);
    }

    public LocationXYZYP() {}

    private LocationXYZYP(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static LocationXYZYP valueOf(Location loc) {
        return new LocationXYZYP(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public static LocationXYZYP valueOf(Vector vector) {
        return LocationXYZYP.of(vector.getX(), vector.getY(), vector.getZ());
    }

    // Format: x, y, z, y, p
    public static LocationXYZYP valueOf(String str) {
        Matcher matcher = PATTERN.matcher(str);
        if (matcher.matches()) {
            LocationXYZYP loc = LocationXYZYP.of(Double.parseDouble(matcher.group("x")),
                    Double.parseDouble(matcher.group("y")),
                    Double.parseDouble(matcher.group("z")));

            if (matcher.groupCount() > 3) {
                loc.setYaw(Float.parseFloat(matcher.group("yaw")));
            }
            if (matcher.groupCount() > 4) {
                loc.setPitch(Float.parseFloat(matcher.group("pitch")));
            }
            return loc;
        }
        return null;
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

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public LocationXYZYP getXYZYP() {
        return this;
    }

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setXYZYP(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Block getBlock(World world) {
        return world.getBlockAt((int) this.x, (int) this.y, (int) this.z);
    }

    public Location toBukkit(World world) {
        return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public void teleport(World world, Player player) {
        player.teleport(this.toBukkit(world));
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public LocationXYZ toLocationXYZ() {
        return LocationXYZ.of(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return this.x + "::" + this.y + "::" + this.z + "::" + this.yaw + "::" + this.pitch;
    }

    @Override
    public LocationXYZYP clone() {
        return new LocationXYZYP(this.x, this.y, this.z, this.yaw, this.pitch);
    }
}
