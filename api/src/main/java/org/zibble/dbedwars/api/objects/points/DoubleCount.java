package org.zibble.dbedwars.api.objects.points;

import org.zibble.dbedwars.api.util.DataType;

public class DoubleCount extends Count<Double> {

    public DoubleCount() {
        super(0.0D, DataType.DOUBLE);
    }

    public DoubleCount(Double value) {
        super(value, DataType.DOUBLE);
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

    @Override
    public Count<Double> clone() {
        return new DoubleCount(this.value);
    }

}
