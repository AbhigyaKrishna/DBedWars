package org.zibble.dbedwars.api.script.action;

import org.zibble.dbedwars.api.script.Translated;

public interface Action extends Translated {

    static Action voidAction() {
        return () -> {};
    }

    void execute();

}
