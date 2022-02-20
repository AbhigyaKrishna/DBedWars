package org.zibble.dbedwars.api.util;

public interface Cancellable {

    void setCancelled(boolean cancelled);

    boolean isCancelled();
}
