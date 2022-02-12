package com.pepedevs.dbedwars.api.future;

import com.pepedevs.dbedwars.api.DBedWarsAPI;
import com.pepedevs.dbedwars.api.task.Workload;
import com.pepedevs.dbedwars.api.util.Duration;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ActionFuture<T> {

    public static <T> ActionFuture<T> supplyAsync(Supplier<? extends T> supplier) {
        return supplyAsync(supplier, null);
    }

    public static <T> ActionFuture<T> supplyAsync(Supplier<? extends T> supplier, Duration delay) {
        ActionFuture<T> future = new ActionFuture<>();
        DBedWarsAPI.getApi().getThreadHandler().submitAsync(new AsyncSupply<>(future, supplier, delay));
        return future;
    }

    public static <T> ActionFuture<T> runAsync(Runnable runnable) {
        return runAsync(runnable, null);
    }

    public static <T> ActionFuture<T> runAsync(Runnable runnable, Duration delay) {
        ActionFuture<T> future = new ActionFuture<>();
        DBedWarsAPI.getApi().getThreadHandler().submitAsync(new AsyncRun(future, runnable, delay));
        return future;
    }

    public static <T> ActionFuture<T> completedFuture(T value) {
        return new ActionFuture<>(value);
    }

    protected T result = null;
    protected Throwable throwable;
    protected Duration delay;
    protected SuccessAction<?, ?> successAction;

    public ActionFuture() {
    }

    public ActionFuture(T value) {
        this.result = value;
    }

    public void complete(T value) {
        this.result = value;
    }

    public void completeExceptionally(Throwable throwable) {
        this.throwable = throwable;
    }

    public T getResult() {
        return result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public ActionFuture<T> delay(Duration delay) {
        this.delay = delay;
        return this;
    }

    public ActionFuture<Void> thenAccept(Consumer<? super T> consumer) {
        ActionFuture<Void> future = new ActionFuture<>();
        this.successAction = SuccessAction.of(this, future, consumer);
        return future;
    }

    public <U> ActionFuture<U> thenApply(Function<? super T, ? extends U> function) {
        ActionFuture<U> future = new ActionFuture<>();
        this.successAction = SuccessAction.of(this, future, function);
        return future;
    }

    public ActionFuture<Void> thenRun(Runnable runnable) {
        ActionFuture<Void> future = new ActionFuture<>();
        this.successAction = SuccessAction.of(this, future, runnable);
        return future;
    }

    public <U> ActionFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> function) {
        ActionFuture<U> future = new ActionFuture<>();
        this.successAction = SuccessAction.ofThrowable(this, future, function);
        return future;
    }

    public <U> ActionFuture<U> exceptionally(Function<Throwable, ? extends U> function) {
        ActionFuture<U> future = new ActionFuture<>();
        this.successAction = SuccessAction.ofThrowable(this, future, function);
        return future;
    }

    protected void postComplete() {
        if (this.successAction != null) {
            DBedWarsAPI.getApi().getThreadHandler().submitAsync(new AsyncRun(this.successAction.getNewFuture(), this.successAction, this.delay));
        }
    }

    static final class AsyncRun implements Workload, Runnable {

        ActionFuture<?> dep;
        Runnable fn;
        Duration delay;

        AsyncRun(ActionFuture<?> dep, Runnable fn, Duration delay) {
            this.dep = dep;
            this.fn = fn;
            this.delay = delay;
        }

        @Override
        public void compute() {
            if (this.delay == null || this.delay.isZero()) {
                this.run();
            } else {
                this.delay = null;
                DBedWarsAPI.getApi().getThreadHandler().runTaskLater(this, this.delay.toMillis());
            }
        }

        @Override
        public void run() {
            try {
                fn.run();
            } catch (Throwable throwable) {
                dep.completeExceptionally(throwable);
            }
            dep.postComplete();
        }

    }

    static final class AsyncSupply<T> implements Workload, Runnable {

        ActionFuture<T> dep;
        Supplier<? extends T> fn;
        Duration delay;

        AsyncSupply(ActionFuture<T> dep, Supplier<? extends T> fn, Duration delay) {
            this.dep = dep;
            this.fn = fn;
            this.delay = delay;
        }

        @Override
        public void compute() {
            if (this.delay == null || this.delay.isZero()) {
                this.run();
            } else {
                this.delay = null;
                DBedWarsAPI.getApi().getThreadHandler().runTaskLater(this, this.delay.toMillis());
            }
        }

        @Override
        public void run() {
            if (dep.result == null) {
                try {
                    dep.complete(fn.get());
                } catch (Throwable throwable) {
                    dep.completeExceptionally(throwable);
                }
            }
            dep.postComplete();
        }

    }

}
