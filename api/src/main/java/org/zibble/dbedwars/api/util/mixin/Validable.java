package org.zibble.dbedwars.api.util.mixin;

/**
 * Simple interface for validating Objects.
 */
public interface Validable {

    /**
     * Gets whether this Object represents a valid instance.
     *
     * <p>
     *
     * @return true if valid
     */
    boolean isValid();

    /**
     * Gets whether this Object represents an invalid instance.
     *
     * <p>
     *
     * @return true if invalid
     */
    default boolean isInvalid() {
        return !this.isValid();
    }

}
