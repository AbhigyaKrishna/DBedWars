package org.zibble.dbedwars.api.util;

import java.util.concurrent.TimeUnit;

/** A duration that can be converted to milliseconds, seconds, minutes, hours or days. */
public class Duration {

    protected long duration;
    protected TimeUnit unit;

    /**
     * Construct duration.
     *
     * <p>
     *
     * @param duration Duration
     * @param unit Time unit
     */
    public Duration(long duration, TimeUnit unit) {
        if (duration <= 0L || unit == null) {
            this.duration = 0L;
            this.unit = TimeUnit.NANOSECONDS;
        } else {
            this.duration = duration;
            this.unit = unit;
        }
    }

    /**
     * Construct duration from milliseconds.
     *
     * <p>
     *
     * @param millis Duration in milliseconds.
     */
    public Duration(long millis) {
        this(millis, TimeUnit.MILLISECONDS);
    }

    /** Represents the zero in the {@link Duration} */
    public static Duration zero() {
        return new Duration(0L, TimeUnit.NANOSECONDS);
    }

    /**
     * Returns a new {@link Duration} that has been created using the given time unit.
     *
     * <p>
     *
     * @param unit Time unit of the duration
     * @param duration the time
     * @return {@link Duration} in the given unit
     */
    public static Duration of(TimeUnit unit, long duration) {
        switch (unit) {
            case DAYS:
                return ofDays(duration);
            case HOURS:
                return ofHours(duration);
            case MICROSECONDS:
                return ofMicroseconds(duration);
            case MILLISECONDS:
                return ofMilliseconds(duration);
            case MINUTES:
                return ofMinutes(duration);
            case NANOSECONDS:
                return ofNanos(duration);
            case SECONDS:
                return ofSeconds(duration);
        }
        return of(TimeUnit.NANOSECONDS, 0L);
    }

    /**
     * Returns a new {@link Duration} that has been created using nanoseconds.
     *
     * <p>
     *
     * @param nanos The nanoseconds
     * @return {@link Duration} in nanoseconds
     */
    public static Duration ofNanos(long nanos) {
        return new Duration(nanos, TimeUnit.NANOSECONDS);
    }

    /**
     * Returns a new {@link Duration} that has been created using microseconds.
     *
     * <p>
     *
     * @param micros The microseconds
     * @return {@link Duration} in microseconds
     */
    public static Duration ofMicroseconds(long micros) {
        return new Duration(micros, TimeUnit.MICROSECONDS);
    }

    /**
     * Returns a new {@link Duration} that has been created using milliseconds.
     *
     * <p>
     *
     * @param millis The milliseconds
     * @return {@link Duration} in milliseconds
     */
    public static Duration ofMilliseconds(long millis) {
        return new Duration(millis, TimeUnit.MILLISECONDS);
    }

    /**
     * Returns a new {@link Duration} that has been created using ticks.
     *
     * <p>
     *
     * @param ticks The ticks
     * @return {@link Duration} in milliseconds
     */
    public static Duration ofTicks(long ticks) {
        return ofMilliseconds(ticks * 50L);
    }

    /**
     * Returns a new {@link Duration} that has been created using milliseconds.
     *
     * <p>
     *
     * @param seconds The seconds
     * @return {@link Duration} in milliseconds
     */
    public static Duration ofSeconds(long seconds) {
        return new Duration(seconds, TimeUnit.SECONDS);
    }

    /**
     * Returns a new {@link Duration} that has been created using minutes.
     *
     * <p>
     *
     * @param minutes The minutes
     * @return {@link Duration} in minutes
     */
    public static Duration ofMinutes(long minutes) {
        return new Duration(minutes, TimeUnit.MINUTES);
    }

    /**
     * Returns a new {@link Duration} that has been created using hours.
     *
     * <p>
     *
     * @param hours The hours
     * @return {@link Duration} in hours
     */
    public static Duration ofHours(long hours) {
        return new Duration(hours, TimeUnit.HOURS);
    }

    /**
     * Returns a new {@link Duration} that has been created using days.
     *
     * <p>
     *
     * @param days The days
     * @return {@link Duration} in days
     */
    public static Duration ofDays(long days) {
        return new Duration(days, TimeUnit.DAYS);
    }

    public static Duration fromJava(java.time.Duration duration) {
        return new Duration(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Returns the original duration.
     *
     * <p>
     *
     * @return Original duration
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Returns the time unit.
     *
     * <p>
     *
     * @return Time unit
     */
    public TimeUnit getUnit() {
        return unit;
    }

    /**
     * Returns the duration converted to nanoseconds.
     *
     * <p>
     *
     * @return Duration in nanoseconds
     */
    public long toNanos() {
        return unit.toNanos(duration);
    }

    /**
     * Returns the duration converted to microseconds.
     *
     * <p>
     *
     * @return Duration in microseconds
     */
    public long toMicros() {
        return unit.toMicros(duration);
    }

    /**
     * Returns the duration converted to milliseconds.
     *
     * <p>
     *
     * @return Duration in milliseconds
     */
    public long toMillis() {
        return unit.toMillis(duration);
    }

    /**
     * Returns the duration converted to seconds.
     *
     * <p>
     *
     * @return Duration in seconds
     */
    public long toSeconds() {
        return unit.toSeconds(duration);
    }

    /**
     * Returns the duration converted to minutes.
     *
     * <p>
     *
     * @return Duration in minutes
     */
    public long toMinutes() {
        return unit.toMinutes(duration);
    }

    /**
     * Returns the duration converted to hours.
     *
     * <p>
     *
     * @return Duration in hours
     */
    public long toHours() {
        return unit.toHours(duration);
    }

    /**
     * Returns the duration converted to days.
     *
     * <p>
     *
     * @return Duration in days
     */
    public long toDays() {
        return unit.toDays(duration);
    }

    /**
     * Returns true if this is zero.
     *
     * <p>
     *
     * @return true if duration is zero, else false
     */
    public boolean isZero() {
        return Duration.zero().equals(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (duration ^ (duration >>> 32));
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            if (obj instanceof Duration) {
                Duration other = (Duration) obj;
                return other.duration == duration && unit == other.unit;
            }
            return false;
        }
    }
}
