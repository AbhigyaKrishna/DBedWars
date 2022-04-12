package org.zibble.dbedwars.api.objects.serializable;

import java.time.DateTimeException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A duration that can be converted to milliseconds, seconds, minutes, hours or days.
 */
public class Duration {

    private static final Pattern YEARS = Pattern.compile("(?<years>\\d+)[y]", Pattern.CASE_INSENSITIVE);
    private static final Pattern MONTHS = Pattern.compile("(?<months>\\d+)mo", Pattern.CASE_INSENSITIVE);
    private static final Pattern WEEKS = Pattern.compile("(?<weeks>\\d+)[w]", Pattern.CASE_INSENSITIVE);
    private static final Pattern DAYS = Pattern.compile("(?<days>\\d+)[d]", Pattern.CASE_INSENSITIVE);
    private static final Pattern HOURS = Pattern.compile("(?<hours>\\d+)[h]", Pattern.CASE_INSENSITIVE);
    private static final Pattern MINUTES = Pattern.compile("(?<minutes>\\d+)m[^o,s]", Pattern.CASE_INSENSITIVE);
    private static final Pattern SECONDS = Pattern.compile("(?<seconds>\\d+)[s]", Pattern.CASE_INSENSITIVE);
    private static final Pattern TICKS = Pattern.compile("(?<ticks>\\d+)[t]", Pattern.CASE_INSENSITIVE);
    private static final Pattern MILLISECONDS = Pattern.compile("(?<milliseconds>\\d+)ms", Pattern.CASE_INSENSITIVE);

    /**
     * Represents the zero in the {@link Duration}
     */
    public static final Duration ZERO = new Duration(0L, TimeUnit.NANOSECONDS);

    protected long duration;
    protected TimeUnit unit;

    public static Duration valueOf(String str) {
        long years = 0;
        long months = 0;
        long weeks = 0;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        long ticks = 0;
        long millis = 0;

        Matcher matcher = YEARS.matcher(str);
        if (matcher.find()) {
            years = Integer.parseInt(matcher.group("years"));
        }

        matcher = MONTHS.matcher(str);
        if (matcher.find()) {
            months = Integer.parseInt(matcher.group("months"));
        }

        matcher = WEEKS.matcher(str);
        if (matcher.find()) {
            weeks = Integer.parseInt(matcher.group("weeks"));
        }

        matcher = DAYS.matcher(str);
        if (matcher.find()) {
            days = Integer.parseInt(matcher.group("days"));
        }

        matcher = HOURS.matcher(str);
        if (matcher.find()) {
            hours = Integer.parseInt(matcher.group("hours"));
        }

        matcher = MINUTES.matcher(str);
        if (matcher.find()) {
            minutes = Integer.parseInt(matcher.group("minutes"));
        }

        matcher = SECONDS.matcher(str);
        if (matcher.find()) {
            seconds = Integer.parseInt(matcher.group("seconds"));
        }

        matcher = TICKS.matcher(str);
        if (matcher.find()) {
            ticks = Integer.parseInt(matcher.group("ticks"));
        }

        matcher = MILLISECONDS.matcher(str);
        if (matcher.find()) {
            millis = Integer.parseInt(matcher.group("milliseconds"));
        }
        return Duration.ofMilliseconds(years * 31536000000L + months * 2628000000L + weeks * 604800000L + days * 86400000L + hours * 3600000L + minutes * 60000L + seconds * 1000L + ticks * 50L + millis);
    }

    public static Duration of(long duration, TimeUnit unit) {
        return new Duration(duration, unit);
    }

    /**
     * Construct duration.
     *
     * <p>
     *
     * @param duration Duration
     * @param unit     Time unit
     */
    private Duration(long duration, TimeUnit unit) {
        if (duration <= 0L || unit == null) {
            this.duration = 0L;
            this.unit = TimeUnit.NANOSECONDS;
        } else {
            this.duration = duration;
            this.unit = unit;
        }
    }

    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        try {
            return ofNanos(startInclusive.until(endExclusive, ChronoUnit.NANOS));
        } catch (DateTimeException | ArithmeticException ex) {
            long secs = startInclusive.until(endExclusive, ChronoUnit.SECONDS);
            long nanos;
            try {
                nanos = endExclusive.getLong(ChronoField.NANO_OF_SECOND) - startInclusive.getLong(ChronoField.NANO_OF_SECOND);
                if (secs > 0 && nanos < 0) {
                    secs++;
                } else if (secs < 0 && nanos > 0) {
                    secs--;
                }
            } catch (DateTimeException ex2) {
                nanos = 0;
            }
            return ofNanos(TimeUnit.SECONDS.toNanos(secs) + nanos);
        }
    }

    /**
     * Returns a new {@link Duration} that has been created using the given time unit.
     *
     * <p>
     *
     * @param unit     Time unit of the duration
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
        return Duration.ZERO.equals(this);
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
