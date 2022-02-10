package com.pepedevs.dbedwars.api.task;

public interface Task {

    int getID();

    void submit(Workload workload);

    void cancel();

    boolean isCancelled();

    boolean isRunning();

}
