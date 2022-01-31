package com.pepedevs.dbedwars.api.objects.points;

public abstract class Count<T extends Number> extends Number {

    protected T value;

    public Count(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public abstract void increment();

    public abstract void decrement();

    public abstract void reset();

    public abstract void add(T delta);

    public abstract void subtract(T delta);

    public abstract void multiply(T delta);

    public abstract void divide(T delta);

    public abstract T incrementAndGet();

    public abstract T getAndIncrement();

    public abstract T decrementAndGet();

    public abstract T getAndDecrement();

    public abstract T addAndGet(T delta);

    public abstract T getAndAdd(T delta);

    public abstract T getAndSet(T newValue);

}