package org.zibble.dbedwars.api.script.condition;

import org.zibble.dbedwars.api.script.Translated;

public interface Condition<T> extends Translated {

    static <T> Condition<T> alwaysTrue() {
        return new Condition<T>() {
            @Override
            public boolean test() {
                return true;
            }

            @Override
            public T getAcceptor() {
                return null;
            }

            @Override
            public void setAcceptor(T acceptor) {

            }
        };
    }

    static <T> Condition<T> alwaysFalse() {
        return new Condition<T>() {
            @Override
            public boolean test() {
                return false;
            }

            @Override
            public T getAcceptor() {
                return null;
            }

            @Override
            public void setAcceptor(T acceptor) {

            }
        };
    }

    boolean test();

    T getAcceptor();

    void setAcceptor(T acceptor);

}
