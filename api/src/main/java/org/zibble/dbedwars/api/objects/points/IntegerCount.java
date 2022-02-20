package org.zibble.dbedwars.api.objects.points;

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

}
