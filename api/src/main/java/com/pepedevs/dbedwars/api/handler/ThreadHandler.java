package com.pepedevs.dbedwars.api.handler;

import com.pepedevs.corelib.task.Workload;
import com.pepedevs.corelib.task.WorkloadThread;

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
