package org.zibble.dbedwars.api.objects.points;

public class DoubleCount extends Count<Double> {

    public DoubleCount() {
        super(0.0D);
    }

    public DoubleCount(Double value) {
        super(value);
    }

    @Override
    public synchronized void increment() {
        this.value++;
    }

    @Override
    public synchronized void decrement() {
        this.value--;
    }

    @Override
    public synchronized void reset() {
        this.value = 0.0D;
    }

    @Override
    public synchronized void add(Double delta) {
        this.value += delta;
    }

    @Override
    public synchronized void subtract(Double delta) {
        this.value -= delta;
    }

    @Override
    public synchronized void multiply(Double delta) {
        this.value *= delta;
    }

    @Override
    public synchronized void divide(Double delta) {
        this.value /= delta;
    }

}
