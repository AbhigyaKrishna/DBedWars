package org.zibble.dbedwars.api.objects.points;

import org.zibble.dbedwars.api.util.DataType;

public class LongCount extends Count<Long> {

    public LongCount() {
        super(0L, DataType.LONG);
    }

    public LongCount(Long value) {
        super(value, DataType.LONG);
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
        this.value = 0L;
    }

    @Override
    public void add(Long delta) {
        this.value += delta;
    }

    @Override
    public void subtract(Long delta) {
        this.value -= delta;
    }

    @Override
    public void multiply(Long delta) {
        this.value *= delta;
    }

    @Override
    public void divide(Long delta) {
        this.value /= delta;
    }

    @Override
    public Count<Long> clone() {
        return new LongCount(this.value);
    }

}
