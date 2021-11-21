package com.pepedevs.dbedwars.api.handler;

import me.Abhigya.core.util.tasks.Workload;
import me.Abhigya.core.util.tasks.WorkloadThread;

import java.util.Collection;

public interface ThreadHandler {

    WorkloadThread[] getSyncThreads();

    WorkloadThread getLeastWorkSyncWorker();

    void submitSync(Workload load);

    void submitSync(Collection<Workload> load);

    WorkloadThread[] getAsyncThreads();

    WorkloadThread getLeastWorkAsyncWorker();

    void submitAsync(Workload load);

    void submitAsync(Collection<Workload> load);
}
