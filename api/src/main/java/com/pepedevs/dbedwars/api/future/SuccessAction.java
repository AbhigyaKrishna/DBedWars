package com.pepedevs.dbedwars.api.future;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class SuccessAction<K, V> implements Runnable {

    protected SuccessActionType type;
    protected ActionFuture<K> future;
    protected ActionFuture<V> newFuture;

    protected SuccessAction(SuccessActionType type, ActionFuture<K> future, ActionFuture<V> newFuture) {
        this.type = type;
        this.future = future;
        this.newFuture = newFuture;
    }

    public SuccessActionType getType() {
        return type;
    }

    public ActionFuture<K> getFuture() {
        return future;
    }

    public ActionFuture<V> getNewFuture() {
        return newFuture;
    }

    static <K, V> SuccessAction<K, V> of(ActionFuture<K> future, ActionFuture<V> newFuture, Consumer<? super K> fn) {
        return new SuccessAction<K, V>(SuccessActionType.ACCEPT, future, newFuture) {
            @Override
            public void run() {
                fn.accept(future.getResult());
            }
        };
    }

    static <K, V> SuccessAction<K, V> of(ActionFuture<K> future, ActionFuture<V> newFuture, Function<? super K, ? extends V> fn) {
        return new SuccessAction<K, V>(SuccessActionType.APPLY, future, newFuture) {
            @Override
            public void run() {
                newFuture.complete(fn.apply(future.getResult()));
            }
        };
    }

    static <K, V> SuccessAction<K, V> of(ActionFuture<K> future, ActionFuture<V> newFuture, Runnable fn) {
        return new SuccessAction<K, V>(SuccessActionType.RUN, future, newFuture) {
            @Override
            public void run() {
                fn.run();
            }
        };
    }

    static <K, V> SuccessAction<K, V> ofThrowable(ActionFuture<K> future, ActionFuture<V> newFuture, BiFunction<? super K, Throwable, ? extends V> fn) {
        return new SuccessAction<K, V>(SuccessActionType.HANDLE, future, newFuture) {
            @Override
            public void run() {
                newFuture.complete(fn.apply(future.getResult(), future.getThrowable()));
            }
        };
    }

    static <K, V> SuccessAction<K, V> ofThrowable(ActionFuture<K> future, ActionFuture<V> newFuture, Function<Throwable, ? extends V> fn) {
        return new SuccessAction<K, V>(SuccessActionType.EXCEPTION, future, newFuture) {
            @Override
            public void run() {
                newFuture.complete(fn.apply(future.getThrowable()));
            }
        };
    }

    static <K, V> SuccessAction<K, V> composeFuture(ActionFuture<K> future, ActionFuture<V> newFuture, Function<? super K, ? extends ActionFuture<V>> fn) {
        return new ComposeSuccessAction<>(future, newFuture, fn);
    }

    protected static class ComposeSuccessAction<K, V> extends SuccessAction<K, V> {

        private final Function<? super K, ? extends ActionFuture<V>> fn;
        protected ActionFuture<V> composedFuture;

        protected ComposeSuccessAction(ActionFuture<K> future, ActionFuture<V> newFuture, Function<? super K, ? extends ActionFuture<V>> fn) {
            super(SuccessActionType.COMPOSE, future, newFuture);
            this.fn = fn;
        }

        @Override
        public void run() {
            this.composedFuture = fn.apply(future.getResult());
        }

        public ActionFuture<V> getComposedFuture() {
            return composedFuture;
        }

    }

    protected enum SuccessActionType {
        ACCEPT,
        APPLY,
        RUN,
        HANDLE,
        EXCEPTION,
        COMPOSE,
        ;
    }

}
