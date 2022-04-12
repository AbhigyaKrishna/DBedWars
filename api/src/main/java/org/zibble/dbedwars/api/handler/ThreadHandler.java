package org.zibble.dbedwars.api.handler;

import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.api.objects.serializable.Duration;

import java.util.Collection;

public interface ThreadHandler {

    void submitSync(Runnable runnable);

    void submitSync(Collection<Runnable> runnable);

    void submitAsync(Workload load);

    void submitAsync(Collection<Workload> load);

    CancellableWorkload runTaskLater(Runnable runnable, Duration delay);

    CancellableWorkload runTaskTimer(Runnable runnable, Duration interval);

}
