package org.zibble.dbedwars.api.handler;

import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.task.Workload;

import java.util.Collection;

public interface ThreadHandler {

    void submitSync(Runnable runnable);

    void submitSync(Collection<Runnable> runnable);

    void submitAsync(Workload load);

    void submitAsync(Collection<Workload> load);

    CancellableWorkload runTaskLater(Runnable runnable, long delayMillis);

    CancellableWorkload runTaskTimer(Runnable runnable, long interval);

}
