package org.zibble.dbedwars.script.condition;

import org.zibble.dbedwars.api.script.condition.Condition;

public class ConditionProcessor<T> {

    private final Condition<T> condition;
    private final T acceptor;

    public ConditionProcessor(Condition<T> condition, T acceptor) {
        this.condition = condition;
        this.acceptor = acceptor;
    }

    public boolean test() {
        return this.condition.test(this.acceptor);
    }

}
