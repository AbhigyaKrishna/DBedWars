package com.pepedevs.dbedwars.api.future;

import com.pepedevs.dbedwars.api.DBedWarsAPI;
import com.pepedevs.dbedwars.api.util.Duration;
import com.pepedevs.radium.utils.scheduler.SchedulerUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ActionFuture<T> {

    private Supplier<? extends T> handle;
    private Duration delay = Duration.of(TimeUnit.MILLISECONDS, 0);
    private ActionFuture<?> after;

    public static <T> ActionFuture<T> supply(Supplier<? extends T> supplier) {
        return new ActionFuture<>(supplier);
    }

    protected ActionFuture(Supplier<? extends T> handle) {
        this.handle = handle;
    }

    public ActionFuture<T> delay(Duration duration) {
        this.delay = duration;
        return this;
    }

    public <U> ActionFuture<U> then(Supplier<? extends U> supplier) {
        this.after = new ActionFuture<>(supplier);
        return (ActionFuture<U>) this.after;
    }

    public void executeSync(BiConsumer<? super T, ? super Throwable> consumer) {
        DBedWarsAPI.getApi().getThreadHandler().runTaskLater(new Runnable() {
            @Override
            public void run() {
                SchedulerUtils.runTask(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            consumer.accept(ActionFuture.this.handle.get(), null);
                        } catch (Throwable throwable) {
                            consumer.accept(null, throwable);
                        }
                    }
                }, DBedWarsAPI.getApi().getPlugin());
                ActionFuture.this.executeSync();
            }
        }, this.delay.toMillis());
    }

    public void executeSync() {
        this.executeSync(new BiConsumer<T, Throwable>() {
            @Override
            public void accept(T t, Throwable throwable) {}
        });
    }

    public void executeAsync(BiConsumer<? super T, ? super Throwable> consumer) {
        DBedWarsAPI.getApi().getThreadHandler().runTaskLater(new Runnable() {
            @Override
            public void run() {
                try {
                    consumer.accept(ActionFuture.this.handle.get(), null);
                } catch (Throwable throwable) {
                    consumer.accept(null, throwable);
                }
                ActionFuture.this.executeAsync();
            }
        }, this.delay.toMillis());
    }

    public void executeAsync() {
        this.executeAsync(new BiConsumer<T, Throwable>() {
            @Override
            public void accept(T t, Throwable throwable) {}
        });
    }

}