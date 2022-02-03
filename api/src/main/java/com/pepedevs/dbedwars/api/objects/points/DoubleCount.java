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

}
