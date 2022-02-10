package com.pepedevs.dbedwars.api.util;

import com.pepedevs.dbedwars.api.npc.BedwarsNPC;

import java.util.function.Consumer;

public interface BedwarsCompletable<T> {

    BedwarsCompletable<T> then(BedwarsCompletable<T> action);

    void executeAsync();

    void executeAsync(Consumer<? super T> success);

    void executeAsync(Consumer<? super BedwarsNPC> success, Consumer<? super Throwable> failure);

    BedwarsCompletable<T> delay(int ticks);

}
