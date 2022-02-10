package com.pepedevs.dbedwars.api.util;

public interface Cancellable {

    void setCancelled(boolean cancelled);

    boolean isCancelled();
}
