package org.zibble.dbedwars.api.objects.math;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Encapsulates an axis aligned bounding box represented by a minimum and a maximum Vector.
 * Additionally, you can query for the bounding box's center, dimensions and corner points.
 */
public class BoundingBox {

    public static final BoundingBox ZERO = new BoundingBox(new Vector(0D, 0D, 0D), new Vector(0D, 0D, 0D));
    public static final BoundingBox BLOCK = new BoundingBox(new Vector(0D, 0D, 0D), new Vector(1D, 1D, 1D));
    public static final BoundingBox INFINITY = new BoundingBox(
            new Vector(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
            new Vector(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
    protected final Vector minimum;
    protected final Vector maximum;
    protected final Vector center;
    protected final Vector dimensions;
    /**
     * Constructs the new bounding box using the provided minimum and maximum vector.
     *
     * @param minimum the minimum vector.
     * @param maximum the maximum vector.
     */
    public BoundingBox(final Vector minimum, final Vector maximum) {
        if (Double.isInfinite(minimum.getX())
                && Double.isInfinite(minimum.getY())
                && Double.isInfinite(minimum.getZ())
                && Double.isInfinite(maximum.getX())
                && Double.isInfinite(maximum.getY())
                && Double.isInfinite(maximum.getZ())) {
            this.minimum = minimum;
            this.maximum = maximum;

            this.center = new Vector(0D, 0D, 0D);
            this.dimensions = new Vector(0D, 0D, 0D);
        } else {
            this.minimum = new Vector(
                    Math.min(minimum.getX(), maximum.getX()),
                    Math.min(minimum.getY(), maximum.getY()),
                    Math.min(minimum.getZ(), maximum.getZ()));

            this.maximum = new Vector(
                    Math.max(minimum.getX(), maximum.getX()),
                    Math.max(minimum.getY(), maximum.getY()),
                    Math.max(minimum.getZ(), maximum.getZ()));

            this.center = this.minimum.clone().add(this.maximum).multiply(0.5F);
            this.dimensions = this.maximum.clone().subtract(this.minimum);
        }
    }

    /**
     * Constructs the new bounding box using the provided minimum and maximum coordinates.
     *
     * <p>
     *
     * @param minimum_x the minimum x.
     * @param minimum_y the minimum y.
     * @param minimum_z the minimum z.
     * @param maximum_x the maximum x.
     * @param maximum_y the maximum y.
     * @param maximum_z the maximum z.
     */
    public BoundingBox(double minimum_x, double minimum_y, double minimum_z, double maximum_x, double maximum_y, double maximum_z) {
        this(new Vector(minimum_x, minimum_y, minimum_z), new Vector(maximum_x, maximum_y, maximum_z));
    }
    /**
     * Constructs the new bounding box incorporating the provided points to calculate the minimum
     * and maximum.
     *
     * <p>
     *
     * @param points the points to incorporate.
     */
    public BoundingBox(Vector... points) {
        BoundingBox temp = INFINITY;
        for (Vector point : points) {
            temp = temp.extend(point);
        }

        this.minimum = temp.minimum;
        this.maximum = temp.maximum;

        this.center = temp.center;
        this.dimensions = temp.dimensions;
    }

    /**
     * Constructs the new bounding box incorporating the provided points to calculate the minimum
     * and maximum.
     *
     * <p>
     *
     * @param points the points to incorporate.
     */
    public BoundingBox(Collection<Vector> points) {
        this(points.toArray(new Vector[0]));
    }

    /**
     * Gets the minimum vector of this bounding box.
     *
     * <p>
     *
     * @return a new copy of the minimum vector.
     */
    public Vector getMinimum() {
        return minimum.clone();
    }

    /**
     * Gets the maximum vector of this bounding box.
     *
     * <p>
     *
     * @return a new copy of the maximum Vector.
     */
    public Vector getMaximum() {
        return maximum.clone();
    }

    /**
     * Gets the center of this bounding box.
     *
     * <p>
     *
     * @return a new copy of the center Vector of this bounding box.
     */
    public Vector getCenter() {
        return center.clone();
    }

    /**
     * Gets the dimensions of this bounding box.
     *
     * <p>
     *
     * @return a new copy of the dimensions Vector of this bounding box.
     */
    public Vector getDimensions() {
        return dimensions.clone();
    }

    /**
     * Gets the width of this bounding box.
     *
     * <p>This is the equivalent of using: {@code BoundingBox.getDimensions().getX()}.
     *
     * <p>
     *
     * @return the width of the bounding box.
     */
    public double getWidth() {
        return dimensions.getX();
    }

    /**
     * Gets the height of this bounding box.
     *
     * <p>This is the equivalent of using: {@code BoundingBox.getDimensions().getY()}.
     *
     * <p>
     *
     * @return the height of the bounding box.
     */
    public double getHeight() {
        return dimensions.getY();
    }

    /**
     * Gets the depth of this bounding box.
     *
     * <p>This is the equivalent of using: {@code BoundingBox.getDimensions().getZ()}.
     *
     * <p>
     *
     * @return the depth of the bounding box.
     */
    public double getDepth() {
        return dimensions.getZ();
    }

    /* BoundingBox Corners */

    public Vector getCorner000() {
        return new Vector(this.minimum.getX(), this.minimum.getY(), this.minimum.getZ());
    }

    public Vector getCorner001() {
        return new Vector(this.minimum.getX(), this.minimum.getY(), this.maximum.getZ());
    }

    public Vector getCorner010() {
        return new Vector(this.minimum.getX(), this.maximum.getY(), this.minimum.getZ());
    }

    public Vector getCorner011() {
        return new Vector(this.minimum.getX(), this.maximum.getY(), this.maximum.getZ());
    }

    public Vector getCorner100() {
        return new Vector(this.maximum.getX(), this.minimum.getY(), this.minimum.getZ());
    }

    public Vector getCorner101() {
        return new Vector(this.maximum.getX(), this.minimum.getY(), this.maximum.getZ());
    }

    public Vector getCorner110() {
        return new Vector(this.maximum.getX(), this.maximum.getY(), this.minimum.getZ());
    }

    public Vector getCorner111() {
        return new Vector(this.maximum.getX(), this.maximum.getY(), this.maximum.getZ());
    }

    public Vector[] getCorners() {
        return new Vector[]{
                this.getCorner000(),
                this.getCorner001(),
                this.getCorner010(),
                this.getCorner011(),
                this.getCorner100(),
                this.getCorner101(),
                this.getCorner110(),
                this.getCorner111()
        };
    }

    /**
     * Adds the provided location {@link Vector}.
     *
     * <p>This is commonly used for locating unit bounding boxes.
     *
     * <p>
     *
     * @param location the location to add.
     * @return a new BoundingBox containing the addition result.
     */
    public BoundingBox add(Vector location) {
        return new BoundingBox(this.minimum.add(location), this.maximum.add(location));
    }

    /**
     * Adds the provided location coordinates.
     *
     * <p>This is commonly used for locating unit bounding boxes.
     *
     * <p>
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     * @return a new BoundingBox containing the addition result.
     */
    public BoundingBox add(double x, double y, double z) {
        return this.add(new Vector(x, y, z));
    }

    /**
     * Subtract by the provided location {@link Vector}.
     *
     * <p>
     *
     * @param location the location to subtract.
     * @return a new BoundingBox containing the subtraction result.
     */
    public BoundingBox subtract(Vector location) {
        return new BoundingBox(this.minimum.subtract(location), this.maximum.subtract(location));
    }

    /**
     * Subtract by the provided location coordinates.
     *
     * <p>
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     * @return a new BoundingBox containing the subtraction result.
     */
    public BoundingBox subtract(double x, double y, double z) {
        return this.subtract(new Vector(x, y, z));
    }

    /**
     * Multiply by the provided location {@link Vector}.
     *
     * <p>
     *
     * @param location the location to multiply.
     * @return a new BoundingBox containing the multiplication result.
     */
    public BoundingBox multiply(Vector location) {
        return new BoundingBox(this.minimum.multiply(location), this.maximum.multiply(location));
    }

    /**
     * Multiply by the provided location coordinates.
     *
     * <p>
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     * @return a new BoundingBox containing the multiplication result.
     */
    public BoundingBox multiply(double x, double y, double z) {
        return this.multiply(new Vector(x, y, z));
    }

    /**
     * Divide by the provided location {@link Vector}.
     *
     * <p>
     *
     * @param location the location to divide.
     * @return a new BoundingBox containing the division result.
     */
    public BoundingBox divide(Vector location) {
        return new BoundingBox(this.minimum.divide(location), this.maximum.divide(location));
    }

    /**
     * Divide by the provided location coordinates.
     *
     * <p>
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     * @return a new BoundingBox containing the division result.
     */
    public BoundingBox divide(double x, double y, double z) {
        return this.divide(new Vector(x, y, z));
    }

    /**
     * Extends this bounding box by the provided bounding box.
     *
     * <p>
     *
     * @param a_bounds the other bounding box.
     * @return a new BoundingBox containing the extension result.
     */
    public BoundingBox extend(BoundingBox a_bounds) {
        return new BoundingBox(new Vector(
                Math.min(this.minimum.getX(), a_bounds.minimum.getX()),
                Math.min(this.minimum.getY(), a_bounds.minimum.getY()),
                Math.min(this.minimum.getZ(), a_bounds.minimum.getZ())
        ), new Vector(
                Math.max(this.maximum.getX(), a_bounds.maximum.getX()),
                Math.max(this.maximum.getY(), a_bounds.maximum.getY()),
                Math.max(this.maximum.getZ(), a_bounds.maximum.getZ())
        ));
    }

    /**
     * Extends this bounding box to incorporate the provided {@link Vector}.
     *
     * <p>
     *
     * @param point the point to incorporate.
     * @return a new BoundingBox containing the extension result.
     */
    public BoundingBox extend(Vector point) {
        return this.extend(point.getX(), point.getY(), point.getZ());
    }

    /**
     * Extends this bounding box to incorporate the provided coordinates.
     *
     * <p>
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param z the z-coordinate.
     * @return a new BoundingBox containing the extension result.
     */
    public BoundingBox extend(double x, double y, double z) {
        return new BoundingBox(new Vector(
                Math.min(this.minimum.getX(), x),
                Math.min(this.minimum.getY(), y),
                Math.min(this.minimum.getZ(), z)
        ), new Vector(
                Math.max(this.maximum.getX(), x),
                Math.max(this.maximum.getY(), y),
                Math.max(this.maximum.getZ(), z)
        ));
    }

    /**
     * Extends this bounding box by the given sphere.
     *
     * <p>
     *
     * @param center the sphere center.
     * @param radius the sphere radius.
     * @return a new BoundingBox containing the extension result.
     */
    public BoundingBox extend(Vector center, double radius) {
        return new BoundingBox(new Vector(
                Math.min(this.minimum.getX(), (center.getX() - radius)),
                Math.min(this.minimum.getY(), (center.getY() - radius)),
                Math.min(this.minimum.getZ(), (center.getZ() - radius))
        ), new Vector(
                Math.max(this.maximum.getX(), (center.getX() + radius)),
                Math.max(this.maximum.getY(), (center.getY() + radius)),
                Math.max(this.maximum.getZ(), (center.getZ() + radius))));
    }

    /**
     * Gets whether the provided {@link BoundingBox} is intersecting this bounding box ( at least
     * one point in ).
     *
     * <p>
     *
     * @param other the bounding box to check.
     * @return true if intersecting.
     */
    public boolean intersects(final BoundingBox other) {
        /* test using SAT (separating axis theorem) */
        double lx = Math.abs(this.center.getX() - other.center.getX());
        double sumx = (this.dimensions.getX() / 2.0F) + (other.dimensions.getX() / 2.0F);

        double ly = Math.abs(this.center.getY() - other.center.getY());
        double sumy = (this.dimensions.getY() / 2.0F) + (other.dimensions.getY() / 2.0F);

        double lz = Math.abs(this.center.getZ() - other.center.getZ());
        double sumz = (this.dimensions.getZ() / 2.0F) + (other.dimensions.getZ() / 2.0F);

        return (lx <= sumx && ly <= sumy && lz <= sumz);
    }

    public boolean contains(final Vector vector) {
        return (this.minimum.getX() <= vector.getX()
                && this.maximum.getX() >= vector.getX()
                && this.minimum.getY() <= vector.getY()
                && this.maximum.getY() >= vector.getY()
                && this.minimum.getZ() <= vector.getZ()
                && this.maximum.getZ() >= vector.getZ());
    }

    public boolean contains(final BoundingBox other) {
        return this.minimum.getX() <= other.minimum.getX()
                && this.minimum.getY() <= other.minimum.getY()
                && this.minimum.getZ() <= other.minimum.getZ()
                && this.maximum.getX() >= other.maximum.getX()
                && this.maximum.getY() >= other.maximum.getY()
                && this.maximum.getZ() >= other.maximum.getZ();
    }

    public Set<Block> getBlocks(World world) {
        Set<Block> blocks = new HashSet<>();
        for (int x = this.minimum.getBlockX(); x <= this.maximum.getBlockX(); x++) {
            for (int y = this.minimum.getBlockY(); y <= this.maximum.getBlockY(); y++) {
                for (int z = this.minimum.getBlockZ(); z <= this.maximum.getBlockZ(); z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public List<Vector> getOutline(double skipDistance) {
        List<Vector> vectors = new ArrayList<>();
        double x, y, z;
        for (x = this.minimum.getX() + skipDistance; x < this.maximum.getX(); x += skipDistance) {
            vectors.add(new Vector(x, this.minimum.getY(), this.minimum.getZ()));
            vectors.add(new Vector(x, this.maximum.getZ(), this.minimum.getZ()));
            vectors.add(new Vector(x, this.minimum.getY(), this.maximum.getZ()));
            vectors.add(new Vector(x, this.maximum.getY(), this.maximum.getZ()));
        }
        for (y = this.minimum.getY() + skipDistance; y < this.maximum.getY(); y += skipDistance) {
            vectors.add(new Vector(this.minimum.getX(), y, this.minimum.getZ()));
            vectors.add(new Vector(this.maximum.getX(), y, this.minimum.getZ()));
            vectors.add(new Vector(this.minimum.getX(), y, this.maximum.getZ()));
            vectors.add(new Vector(this.maximum.getX(), y, this.maximum.getZ()));
        }
        for (z = this.minimum.getZ() + skipDistance; z < this.maximum.getZ(); z += skipDistance) {
            vectors.add(new Vector(this.minimum.getX(), this.minimum.getY(), z));
            vectors.add(new Vector(this.maximum.getX(), this.minimum.getY(), z));
            vectors.add(new Vector(this.minimum.getX(), this.maximum.getY(), z));
            vectors.add(new Vector(this.maximum.getX(), this.maximum.getY(), z));
        }
        vectors.addAll(Arrays.asList(this.getCorners()));
        return vectors;
    }

    public boolean isValid() {
        return this.minimum.getX() <= this.maximum.getX()
                && this.minimum.getY() <= this.maximum.getY()
                && this.minimum.getZ() <= this.maximum.getZ();
    }

    @Override
    public String toString() {
        return "Box [ " + this.minimum.toString().replace(",", "::") + " -> " + this.maximum.toString().replace(",", "::") + " ]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.center == null) ? 0 : this.center.hashCode());
        result = prime * result + ((this.dimensions == null) ? 0 : this.dimensions.hashCode());
        result = prime * result + ((this.maximum == null) ? 0 : this.maximum.hashCode());
        result = prime * result + ((this.minimum == null) ? 0 : this.minimum.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof BoundingBox)) {
            return false;
        }

        BoundingBox other = (BoundingBox) obj;
        if (!this.maximum.equals(other.maximum) || !this.minimum.equals(other.minimum)) {
            return false;
        }

        return this.center.equals(other.center) && this.dimensions.equals(other.dimensions);
    }

}
