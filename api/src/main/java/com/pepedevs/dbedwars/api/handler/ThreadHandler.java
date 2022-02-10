package com.pepedevs.dbedwars.api.handler;

import com.pepedevs.dbedwars.api.task.Workload;

import java.util.Collection;

public interface ThreadHandler {

    void submitSync(Runnable runnable);

    void submitSync(Collection<Runnable> runnable);

    void submitAsync(Workload load);

    void submitAsync(Collection<Workload> load);
}
