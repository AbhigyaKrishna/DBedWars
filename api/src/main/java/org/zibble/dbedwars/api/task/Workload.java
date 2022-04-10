package org.zibble.dbedwars.api.task;

/**
 * An interface to determine works.
 */
public interface Workload {

    /**
     * Computes a piece of the work.
     */
    void compute();

    /**
     * Computes then checks {@link #reSchedule()}.
     *
     * <p>
     *
     * @return {@code true} if re-scheduling
     */
    default boolean computeThenCheckForScheduling() {
        this.compute();
        return !this.reSchedule();
    }

    /**
     * Checks if the work is reSchedulable.
     *
     * <p>
     *
     * @return {@code true} if the work should be re schedule
     */
    default boolean reSchedule() {
        return false;
    }

    /**
     * Checks if the work should execute. if it's {@code true} than runs {@link Workload#compute()}.
     *
     * <p>
     *
     * @return {@code true} if the work should execute
     */
    default boolean shouldExecute() {
        return true;
    }

}
