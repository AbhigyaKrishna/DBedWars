package com.pepedevs.dbedwars.api.objects.points;

public class IntegerCount extends Count<Integer> {

    public IntegerCount() {
        super(0);
    }

    public IntegerCount(Integer value) {
        super(value);
    }

    @Override
    public void increment() {
        this.value++;
    }

    @Override
    public void decrement() {
        this.value--;
    }

    @Override
    public void reset() {
        this.value = 0;
    }

    @Override
    public void add(Integer delta) {
        this.value += delta;
    }

    @Override
    public void subtract(Integer delta) {
        this.value -= delta;
    }

    @Override
    public void multiply(Integer delta) {
        this.value *= delta;
    }

    @Override
    public void divide(Integer delta) {
        this.value /= delta;
    }

    @Override
    public Integer incrementAndGet() {
        return ++this.value;
    }

    @Override
    public Integer getAndIncrement() {
        return this.value++;
    }

    @Override
    public Integer decrementAndGet() {
        return --this.value;
    }

    @Override
    public Integer getAndDecrement() {
        return this.value--;
    }

    @Override
    public Integer addAndGet(Integer delta) {
        return this.value += delta;
    }

    @Override
    public Integer getAndAdd(Integer delta) {
        Integer oldValue = this.value;
        this.value += delta;
        return oldValue;
    }

    @Override
    public Integer getAndSet(Integer newValue) {
        Integer oldValue = this.value;
        this.value = newValue;
        return oldValue;
    }

    @Override
    public int intValue() {
        return this.get();
    }

    @Override
    public long longValue() {
        return this.get();
    }

    @Override
    public float floatValue() {
        return this.get();
    }

    @Override
    public double doubleValue() {
        return this.get();
    }

}
