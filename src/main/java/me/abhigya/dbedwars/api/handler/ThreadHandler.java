package me.abhigya.dbedwars.api.handler;

import me.Abhigya.core.util.tasks.Workload;
import me.Abhigya.core.util.tasks.WorkloadThread;

import java.util.Collection;

public interface ThreadHandler {

    WorkloadThread[] getSyncThreads( );

    WorkloadThread getLeastWorkSyncWorker( );

    void addSyncWork( Workload work );

    void addSyncWork( Collection< Workload > work );

    WorkloadThread[] getAsyncThreads( );

    WorkloadThread getLeastWorkAsyncWorker( );

    void addAsyncWork( Workload work );

    void addAsyncWork( Collection< Workload > work );

}
