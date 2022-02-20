package org.zibble.dbedwars.api.objects.points;

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

    public T incrementAndGet() {
        this.increment();
        return this.get();
    }

    public T getAndIncrement() {
        T oldValue = this.get();
        this.increment();
        return oldValue;
    }

    public T decrementAndGet() {
        this.decrement();
        return this.get();
    }

    public T getAndDecrement() {
        T oldValue = this.get();
        this.decrement();
        return oldValue;
    }

    public T addAndGet(T delta) {
        this.add(delta);
        return this.get();
    }

    public T getAndAdd(T delta) {
        T oldValue = this.get();
        this.add(delta);
        return oldValue;
    }

    public T getAndSet(T newValue) {
        T oldValue = this.get();
        this.set(newValue);
        return oldValue;
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

}