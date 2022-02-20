package org.zibble.dbedwars.api.action;

public interface Action<T> {

    void execute();

    T getHandle();

}
