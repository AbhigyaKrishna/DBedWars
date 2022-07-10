package org.zibble.dbedwars.api.future;

import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.api.objects.serializable.Duration;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ActionFuture<T> {

    private static boolean LOG_EXCEPTION = false;

    public static void setLogException(boolean logException) {
        LOG_EXCEPTION = logException;
    }

    protected T result = null;
    protected Throwable throwable;
    protected boolean completed = false;
    protected Duration delay;
    protected SuccessAction<?, ?> successAction;

    public ActionFuture() {
    }
    public ActionFuture(T value) {
        this.result = value;
        this.completed = true;
    }

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

    public void complete(T value) {
        this.result = value;
        this.completed = true;
    }

    public void completeExceptionally(Throwable throwable) {
        this.throwable = throwable;
        this.completed = true;
    }

    public T getResult() {
        return this.result;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public boolean isCompleted() {
        return this.completed;
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

        if (this.completed) {
            this.postComplete();
        }

        return future;
    }

    public ActionFuture<Void> thenRun(Runnable runnable) {
        ActionFuture<Void> future = new ActionFuture<>();
        this.successAction = SuccessAction.of(this, future, runnable);

        if (this.completed) {
            this.postComplete();
        }

        return future;
    }

    public <U> ActionFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> function) {
        ActionFuture<U> future = new ActionFuture<>();
        this.successAction = SuccessAction.ofThrowable(this, future, function);

        if (this.completed) {
            this.postComplete();
        }

        return future;
    }

    public <U> ActionFuture<U> exceptionally(Function<Throwable, ? extends U> function) {
        ActionFuture<U> future = new ActionFuture<>();
        this.successAction = SuccessAction.ofThrowable(this, future, function);

        if (this.completed) {
            this.postComplete();
        }

        return future;
    }

    public <U> ActionFuture<U> thenCompose(Function<? super T, ? extends ActionFuture<U>> function) {
        ActionFuture<U> future = new ActionFuture<>();
        this.successAction = SuccessAction.composeFuture(this, future, function);

        if (this.completed) {
            this.postComplete();
        }

        return future;
    }

    protected void postComplete() {
        this.completed = true;
        if (this.successAction != null) {
            if (this.successAction.getType() == SuccessAction.SuccessActionType.COMPOSE) {
                DBedWarsAPI.getApi().getThreadHandler().submitAsync(new ComposeFuture<>(this.successAction.getNewFuture(), this.successAction, this.delay));
            } else {
                DBedWarsAPI.getApi().getThreadHandler().submitAsync(new AsyncRun(this.successAction.getNewFuture(), this.successAction, this.delay));
            }
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
                DBedWarsAPI.getApi().getThreadHandler().runTaskLater(this, this.delay);
                this.delay = null;
            }
        }

        @Override
        public void run() {
            if (!this.dep.isCompleted()) {
                try {
                    this.fn.run();
                } catch (Throwable throwable) {
                    this.dep.completeExceptionally(throwable);
                    if (LOG_EXCEPTION)
                        throwable.printStackTrace();
                }
            }
            this.dep.postComplete();
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
                DBedWarsAPI.getApi().getThreadHandler().runTaskLater(this, this.delay);
                this.delay = null;
            }
        }

        @Override
        public void run() {
            if (!this.dep.isCompleted()) {
                try {
                    this.dep.complete(this.fn.get());
                } catch (Throwable throwable) {
                    this.dep.completeExceptionally(throwable);
                    if (LOG_EXCEPTION)
                        throwable.printStackTrace();
                }
            }
            this.dep.postComplete();
        }

    }

    static final class ComposeFuture<V> extends CancellableWorkload {

        final long start = System.currentTimeMillis();
        ActionFuture<V> dep;
        SuccessAction.ComposeSuccessAction<?, V> fn;
        Duration delay;
        private boolean delayed = false;

        ComposeFuture(ActionFuture<V> dep, SuccessAction<?, ?> fn, Duration delay) {
            this.dep = dep;
            this.fn = (SuccessAction.ComposeSuccessAction<?, V>) fn;
            this.delay = delay == null ? Duration.ZERO : delay;
        }

        @Override
        public void compute() {
            this.delayed = true;
            if (this.fn.getComposedFuture() == null) {
                this.fn.run();
            }

            if (this.fn.getComposedFuture().isCompleted()) {
                this.setCancelled(true);
                this.dep.complete(this.fn.getComposedFuture().getResult());
                this.dep.throwable = this.fn.getComposedFuture().throwable;
                if (LOG_EXCEPTION && this.fn.getComposedFuture().throwable != null)
                    this.fn.getComposedFuture().throwable.printStackTrace();
                this.dep.postComplete();
            }
        }

        @Override
        public boolean shouldExecute() {
            return !this.isCancelled() && (this.delayed || System.currentTimeMillis() - this.start >= this.delay.toMillis());
        }

    }

}
