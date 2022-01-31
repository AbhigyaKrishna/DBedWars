package com.pepedevs.dbedwars.api.objects.points;

public class DoubleCount extends Count<Double> {

    public DoubleCount() {
        super(0.0D);
    }

    public DoubleCount(Double value) {
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
        this.value = 0.0D;
    }

    @Override
    public void add(Double delta) {
        this.value += delta;
    }

    @Override
    public void subtract(Double delta) {
        this.value -= delta;
    }

    @Override
    public void multiply(Double delta) {
        this.value *= delta;
    }

    @Override
    public void divide(Double delta) {
        this.value /= delta;
    }

    @Override
    public Double incrementAndGet() {
        return ++this.value;
    }

    @Override
    public Double getAndIncrement() {
        return this.value++;
    }

    @Override
    public Double decrementAndGet() {
        return --this.value;
    }

    @Override
    public Double getAndDecrement() {
        return this.value--;
    }

    @Override
    public Double addAndGet(Double delta) {
        return this.value += delta;
    }

    @Override
    public Double getAndAdd(Double delta) {
        Double oldValue = this.value;
        this.value += delta;
        return oldValue;
    }

    @Override
    public Double getAndSet(Double newValue) {
        Double oldValue = this.value;
        this.value = newValue;
        return oldValue;
    }

    @Override
    public int intValue() {
        return (int) (double) this.get();
    }

    @Override
    public long longValue() {
        return (long) (double) this.get();
    }

    @Override
    public float floatValue() {
        return (float) (double) this.get();
    }

    @Override
    public double doubleValue() {
        return this.get();
    }

}
