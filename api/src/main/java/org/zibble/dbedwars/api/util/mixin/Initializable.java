package org.zibble.dbedwars.api.util.mixin;

/**
 * Simple interface that represents Objects that can be initialized.
 */
public interface Initializable {

    /**
     * Gets whether this Object is initialized.
     *
     * <p>
     *
     * @return true if initialized
     */
    public boolean isInitialized();

}
