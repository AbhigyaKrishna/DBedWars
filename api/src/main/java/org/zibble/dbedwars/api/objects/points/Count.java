package org.zibble.dbedwars.api.objects.points;

import org.zibble.dbedwars.api.util.DataType;

public abstract class Count<T extends Number> extends Number implements Cloneable {

    protected T value;
    private final DataType type;

    public Count(T value, DataType type) {
        this.value = value;
        this.type = type;
    }

    public T get() {
        return value;
    }

    public synchronized void set(T value) {
        this.value = value;
    }

    public abstract void increment();

    public abstract void decrement();

    public abstract void reset();

    public abstract void add(T delta);

    public abstract void subtract(T delta);

    public abstract void multiply(T delta);

    public abstract void divide(T delta);

    public synchronized T incrementAndGet() {
        this.increment();
        return this.get();
    }

    public synchronized T getAndIncrement() {
        T oldValue = this.get();
        this.increment();
        return oldValue;
    }

    public synchronized T decrementAndGet() {
        this.decrement();
        return this.get();
    }

    public synchronized T getAndDecrement() {
        T oldValue = this.get();
        this.decrement();
        return oldValue;
    }

    public synchronized T addAndGet(T delta) {
        this.add(delta);
        return this.get();
    }

    public synchronized T getAndAdd(T delta) {
        T oldValue = this.get();
        this.add(delta);
        return oldValue;
    }

    public synchronized T getAndSet(T newValue) {
        T oldValue = this.get();
        this.set(newValue);
        return oldValue;
    }

    public DataType getType() {
        return this.type;
    }

    @Override
    public int intValue() {
        return this.value.intValue();
    }

    @Override
    public long longValue() {
        return this.value.longValue();
    }

    @Override
    public float floatValue() {
        return this.value.floatValue();
    }

    @Override
    public double doubleValue() {
        return this.value.doubleValue();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Count && this.value.equals(((Count) obj).value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public abstract Count<T> clone();

}