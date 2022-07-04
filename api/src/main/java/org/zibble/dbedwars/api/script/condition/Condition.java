package org.zibble.dbedwars.api.script.condition;

import org.zibble.dbedwars.api.script.Translated;

public interface Condition extends Translated {

    static Condition alwaysTrue() {
        return () -> true;
    }

    static Condition alwaysFalse() {
        return () -> false;
    }

    boolean test();

}
