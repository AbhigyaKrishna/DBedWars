package org.zibble.dbedwars.api.script.action;

import org.zibble.dbedwars.api.script.Translated;

public interface Action<T> extends Translated {

    void execute();

    T getHandle();

}
