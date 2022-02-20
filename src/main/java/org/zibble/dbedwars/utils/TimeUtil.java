package org.zibble.dbedwars.utils;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    private static final Matcher YEARS = Pattern.compile("(?<years>\\d+([y]))", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher MONTHS = Pattern.compile("(?<months>\\d+(mo))", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher WEEKS = Pattern.compile("(?<weeks>\\d+([w]))", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher DAYS = Pattern.compile("(?<days>\\d+([d]))", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher HOURS = Pattern.compile("(?<hours>\\d+([h]))", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher MINUTES = Pattern.compile("(?<minutes>\\d+(m[^o,s]))", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher SECONDS = Pattern.compile("(?<seconds>\\d+([s]))", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher TICKS = Pattern.compile("(?<ticks>\\d+([t]))", Pattern.CASE_INSENSITIVE).matcher("");
    private static final Matcher MILLISECONDS = Pattern.compile("(?<milliseconds>\\d+(ms))", Pattern.CASE_INSENSITIVE).matcher("");

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

        YEARS.reset(time);
        if (YEARS.find()) {
            years = Integer.parseInt(YEARS.group("years").replace("y", ""));
        }

        MONTHS.reset(time);
        if (MONTHS.find()) {
            months = Integer.parseInt(MONTHS.group("months").replace("m", ""));
        }

        WEEKS.reset(time);
        if (WEEKS.find()) {
            weeks = Integer.parseInt(WEEKS.group("weeks").replace("w", ""));
        }

        DAYS.reset(time);
        if (DAYS.find()) {
            days = Integer.parseInt(DAYS.group("days").replace("d", ""));
        }

        HOURS.reset(time);
        if (HOURS.find()) {
            hours = Integer.parseInt(HOURS.group("hours").replace("h", ""));
        }

        MINUTES.reset(time);
        if (MINUTES.find()) {
            minutes = Integer.parseInt(MINUTES.group("minutes").replace("m", ""));
        }

        SECONDS.reset(time);
        if (SECONDS.find()) {
            seconds = Integer.parseInt(SECONDS.group("seconds").replace("s", ""));
        }

        TICKS.reset(time);
        if (TICKS.find()) {
            ticks = Integer.parseInt(TICKS.group("ticks").replace("t", ""));
        }

        MILLISECONDS.reset(time);
        if (MILLISECONDS.find()) {
            millis = Integer.parseInt(MILLISECONDS.group("milliseconds").replace("ms", ""));
        }
        return Duration.ofMillis(years * 31536000000L + months * 2628000000L + weeks * 604800000L + days * 86400000L + hours * 3600000L + minutes * 60000L + seconds * 1000L + ticks * 50L + millis);
    }

}
