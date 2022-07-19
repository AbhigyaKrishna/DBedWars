package org.zibble.dbedwars.api.objects.points;

import org.zibble.dbedwars.api.util.DataType;

public class IntegerCount extends Count<Integer> {

    public IntegerCount() {
        super(0, DataType.INTEGER);
    }

    public IntegerCount(Integer value) {
        super(value, DataType.INTEGER);
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
        this.value = 0;
    }

    @Override
    public synchronized void add(Integer delta) {
        this.value += delta;
    }

    @Override
    public synchronized void subtract(Integer delta) {
        this.value -= delta;
    }

    @Override
    public synchronized void multiply(Integer delta) {
        this.value *= delta;
    }

    @Override
    public synchronized void divide(Integer delta) {
        this.value /= delta;
    }

    @Override
    public Count<Integer> clone() {
        return new IntegerCount(this.value);
    }

}
