package com.pepedevs.dbedwars.api.future;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SuccessAction<K, V> implements Runnable {

    protected ActionFuture<K> future;
    protected ActionFuture<V> newFuture;

    protected SuccessAction(ActionFuture<K> future, ActionFuture<V> newFuture) {
        this.future = future;
        this.newFuture = newFuture;
    }

    public ActionFuture<K> getFuture() {
        return future;
    }

    public ActionFuture<V> getNewFuture() {
        return newFuture;
    }

    static <K, V> SuccessAction<K, V> of(ActionFuture<K> future, ActionFuture<V> newFuture, Consumer<? super K> fn) {
        return new SuccessAction<K, V>(future, newFuture) {
            @Override
            public void run() {
                fn.accept(future.getResult());
            }
        };
    }

    static <K, V> SuccessAction<K, V> of(ActionFuture<K> future, ActionFuture<V> newFuture, Function<? super K, ? extends V> fn) {
        return new SuccessAction<K, V>(future, newFuture) {
            @Override
            public void run() {
                newFuture.completeFuture(fn.apply(future.getResult()));
            }
        };
    }

    static <K, V> SuccessAction<K, V> of(ActionFuture<K> future, ActionFuture<V> newFuture, Runnable fn) {
        return new SuccessAction<K, V>(future, newFuture) {
            @Override
            public void run() {
                fn.run();
            }
        };
    }

}
