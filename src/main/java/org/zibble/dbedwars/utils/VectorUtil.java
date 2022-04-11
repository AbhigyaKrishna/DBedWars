package org.zibble.dbedwars.utils;

import org.bukkit.util.Vector;

public class VectorUtil {

    /**
     * Generates a random unit vector.
     *
     * <p>
     *
     * @return New random unit vector.
     */
    public static Vector getRandomVector() {
        double x = Math.random() * 2 - 1;
        double y = Math.random() * 2 - 1;
        double z = Math.random() * 2 - 1;

        return new Vector(x, y, z).normalize();
    }

    /**
     * Generates a vector pointing to a random direction over a circle.
     *
     * <p>
     *
     * @return Random vector.
     */
    public static Vector getRandomCircleVector() {
        double rnd = Math.random() * 2 * Math.PI;

        double x = Math.cos(rnd);
        double z = Math.sin(rnd);

        return new Vector(x, 0, z);
    }

    /**
     * Rotates a vector around the X axis at an angle
     *
     * <p>
     *
     * @param v Starting vector
     * @param angle How much to rotate
     * @return The starting vector rotated
     */
    public static Vector rotateAroundAxisX(Vector v, double angle) {
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    /**
     * Rotates a vector around the Y axis at an angle
     *
     * <p>
     *
     * @param v Starting vector
     * @param angle How much to rotate
     * @return The starting vector rotated
     */
    public static Vector rotateAroundAxisY(Vector v, double angle) {
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    /**
     * Rotates a vector around the Z axis at an angle
     *
     * <p>
     *
     * @param v Starting vector
     * @param angle How much to rotate
     * @return The starting vector rotated
     */
    public static Vector rotateAroundAxisZ(Vector v, double angle) {
        double x, y, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos - v.getY() * sin;
        y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    /**
     * Rotates a vector around the X, Y, and Z axes
     *
     * <p>
     *
     * @param v The starting vector
     * @param angleX The change angle on X
     * @param angleY The change angle on Y
     * @param angleZ The change angle on Z
     * @return The starting vector rotated
     */
    public static Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
        rotateAroundAxisX(v, angleX);
        rotateAroundAxisY(v, angleY);
        rotateAroundAxisZ(v, angleZ);
        return v;
    }

    /**
     * This handles non-unit vectors, with yaw and pitch instead of X,Y,Z angles.
     *
     * <p>
     *
     * @param vector The starting vector
     * @param yawDegrees The yaw offset in degrees
     * @param pitchDegrees The pitch offset in degrees
     * @return The starting vector rotated
     */
    public static Vector rotateVector(Vector vector, float yawDegrees, float pitchDegrees) {
        // get radians.
        double yaw = Math.toRadians(-yawDegrees);
        double pitch = Math.toRadians(-pitchDegrees);

        // get yaw/pitch sine and cosine.
        double cosYaw = Math.cos(yaw);
        double cosPitch = Math.cos(pitch);
        double sinYaw = Math.sin(yaw);
        double sinPitch = Math.sin(pitch);

        // axis.
        double initialX, initialY, initialZ;
        double x, y, z;

        // Z_axis rotation (Pitch)
        initialX = vector.getX();
        initialY = vector.getY();
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        // Y_axis rotation (Yaw)
        initialZ = vector.getZ();
        initialX = x;
        z = initialZ * cosYaw - initialX * sinYaw;
        x = initialZ * sinYaw + initialX * cosYaw;

        // return a new vector with calculated axis.
        return new Vector(x, y, z);
    }

}
