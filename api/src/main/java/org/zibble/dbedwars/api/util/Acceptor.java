package org.zibble.dbedwars.api.util;

/** A value tester for accepting/reject values. */
public interface Acceptor<T> {

    /**
     * Tests whether the provided value should be accepted or not.
     *
     * <p>
     *
     * @param value Value to test.
     * @return Whether the provided value is accepted or not.
     */
    public boolean accept(T value);
}
