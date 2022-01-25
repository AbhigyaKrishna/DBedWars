package com.pepedevs.dbedwars.api.util;

import java.util.function.Consumer;

public interface BedwarsAction<T> {

    BedwarsAction<T> then(BedwarsAction<T> action);

    void executeAsync();

    void executeAsync(Consumer<? super T> success);

    BedwarsAction<T> delay(int ticks);

}
