package org.zibble.dbedwars.utils;

import org.zibble.dbedwars.api.util.Duration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    private static final Pattern YEARS = Pattern.compile("(?<years>\\d+([y]))", Pattern.CASE_INSENSITIVE);
    private static final Pattern MONTHS = Pattern.compile("(?<months>\\d+(mo))", Pattern.CASE_INSENSITIVE);
    private static final Pattern WEEKS = Pattern.compile("(?<weeks>\\d+([w]))", Pattern.CASE_INSENSITIVE);
    private static final Pattern DAYS = Pattern.compile("(?<days>\\d+([d]))", Pattern.CASE_INSENSITIVE);
    private static final Pattern HOURS = Pattern.compile("(?<hours>\\d+([h]))", Pattern.CASE_INSENSITIVE);
    private static final Pattern MINUTES = Pattern.compile("(?<minutes>\\d+(m[^o,s]))", Pattern.CASE_INSENSITIVE);
    private static final Pattern SECONDS = Pattern.compile("(?<seconds>\\d+([s]))", Pattern.CASE_INSENSITIVE);
    private static final Pattern TICKS = Pattern.compile("(?<ticks>\\d+([t]))", Pattern.CASE_INSENSITIVE);
    private static final Pattern MILLISECONDS = Pattern.compile("(?<milliseconds>\\d+(ms))", Pattern.CASE_INSENSITIVE);

    public static Duration parse(String time) {
        long years = 0;
        long months = 0;
        long weeks = 0;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        long ticks = 0;
        long millis = 0;

        Matcher matcher = YEARS.matcher(time);
        if (matcher.find()) {
            years = Integer.parseInt(matcher.group("years").replace("y", ""));
        }

        matcher = MONTHS.matcher(time);
        if (matcher.find()) {
            months = Integer.parseInt(matcher.group("months").replace("m", ""));
        }

        matcher = WEEKS.matcher(time);
        if (matcher.find()) {
            weeks = Integer.parseInt(matcher.group("weeks").replace("w", ""));
        }

        matcher = DAYS.matcher(time);
        if (matcher.find()) {
            days = Integer.parseInt(matcher.group("days").replace("d", ""));
        }

        matcher = HOURS.matcher(time);
        if (matcher.find()) {
            hours = Integer.parseInt(matcher.group("hours").replace("h", ""));
        }

        matcher = MINUTES.matcher(time);
        if (matcher.find()) {
            minutes = Integer.parseInt(matcher.group("minutes").replace("m", ""));
        }

        matcher = SECONDS.matcher(time);
        if (matcher.find()) {
            seconds = Integer.parseInt(matcher.group("seconds").replace("s", ""));
        }

        matcher = TICKS.matcher(time);
        if (matcher.find()) {
            ticks = Integer.parseInt(matcher.group("ticks").replace("t", ""));
        }

        matcher = MILLISECONDS.matcher(time);
        if (matcher.find()) {
            millis = Integer.parseInt(matcher.group("milliseconds").replace("ms", ""));
        }
        return Duration.ofMilliseconds(years * 31536000000L + months * 2628000000L + weeks * 604800000L + days * 86400000L + hours * 3600000L + minutes * 60000L + seconds * 1000L + ticks * 50L + millis);
    }

}
