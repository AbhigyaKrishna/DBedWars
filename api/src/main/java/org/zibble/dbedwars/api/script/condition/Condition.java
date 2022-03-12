package org.zibble.dbedwars.api.script.condition;

import org.zibble.dbedwars.api.script.Translated;

import java.util.function.Predicate;

public interface Condition<T> extends Predicate<T>, Translated {

    static <T> Condition<T> alwaysTrue() {
        return __ -> true;
    }

    static <T> Condition<T> alwaysFalse() {
        return __ -> false;
    }

}
