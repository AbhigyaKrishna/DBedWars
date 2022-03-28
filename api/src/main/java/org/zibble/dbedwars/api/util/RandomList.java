package org.zibble.dbedwars.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class RandomList<T> extends ArrayList<T> {

    public RandomList() {
    }

    public RandomList(int initialCapacity) {
        super(initialCapacity);
    }

    public RandomList(@NotNull Collection<? extends T> c) {
        super(c);
    }

    public T randomValue() {
        return this.randomValue(ThreadLocalRandom.current());
    }

    public T randomValue(Random random) {
        return this.get(random.nextInt(size()));
    }

    public T randomValue(Predicate<T> predicate) {
        return this.randomValue(ThreadLocalRandom.current(), predicate);
    }

    public T randomValue(Random random, Predicate<T> predicate) {
        T value;
        do {
            value = this.get(random.nextInt(size()));
        } while (predicate != null && !predicate.test(value));
        return value;
    }

}
