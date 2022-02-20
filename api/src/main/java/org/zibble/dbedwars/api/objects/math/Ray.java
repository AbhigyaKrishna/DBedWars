package org.zibble.dbedwars.api.objects.math;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Objects;

/** Encapsulates an immutable ray having a starting position and a unit length direction. */
public class Ray implements Cloneable {

    protected final Vector origin;
    protected final Vector direction;

    /**
     * Constructs the ray.
     *
     * <p>
     *
     * @param origin Starting position
     * @param direction Direction
     */
    public Ray(final Vector origin, final Vector direction) {
        this.origin = origin.clone();
        this.direction = direction.clone().normalize();
    }

    /**
     * Constructs the ray.
     *
     * <p>
     *
     * @param origin Starting position
     * @param direction Direction
     */
    public Ray(final Location origin, final Vector direction) {
        this(origin.toVector(), direction);
    }

    /**
     * Constructs the ray from the provided {@link Location}, setting the {@link #origin} from its
     * position coordinates (x, y and z), and the {@link #direction} from its direction.
     *
     * <p>
     *
     * @param data Location
     */
    public Ray(final Location data) {
        this(data, data.getDirection());
    }

    /**
     * Gets the ray's origin location vector ( a vector representing a location ).
     *
     * <p>
     *
     * @return New copy of the ray's origin.
     */
    public Vector getOrigin() {
        return this.origin.clone();
    }

    /**
     * Gets the ray's direction vector.
     *
     * <p>
     *
     * @return New copy of the ray's origin
     */
    public Vector getDirection() {
        return this.direction.clone();
    }

    /**
     * Returns the endpoint given the distance. This is calculated as startpoint + distance *
     * direction.
     *
     * <p>
     *
     * @param distance Distance from the end point to the start point
     * @return Endpoint result
     */
    public Vector getEndPoint(final double distance) {
        return this.direction.clone().multiply(distance).add(this.origin);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Ray) {
            return Objects.equals(((Ray) obj).origin, this.origin) && Objects.equals(((Ray) obj).direction, this.direction);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 73;
        int result = 1;
        result = prime * result + this.direction.hashCode();
        result = prime * result + this.origin.hashCode();
        return result;
    }

    @Override
    public Ray clone() {
        return new Ray(this.origin, this.direction);
    }

}
