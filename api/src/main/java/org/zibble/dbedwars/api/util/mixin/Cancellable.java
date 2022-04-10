package org.zibble.dbedwars.api.util.mixin;

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
