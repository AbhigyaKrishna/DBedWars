package org.zibble.dbedwars.api.objects.serializable;

import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.zibble.dbedwars.api.messaging.AbstractMessaging;
import org.zibble.dbedwars.api.messaging.Messaging;
import org.zibble.dbedwars.api.messaging.message.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleST implements Cloneable {

    public static final long DEFAULT_FADE_IN = 10L;
    public static final long DEFAULT_STAY = 70L;
    public static final long DEFAULT_FADE_OUT = 20L;
    private static final Pattern PATTERN = Pattern.compile("^(?<title>.+?(?=::|$))(?:::[+-]?(?<fadein>\\d*\\.?\\d*)::[+-]?(?<stay>\\d*\\.?\\d*)::[+-]?(?<fadeout>\\d*\\.?\\d*))?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^(?<title>.+?(?=\\\\n|$))(?:\\\\n(?<subtitle>.*))?$", Pattern.CASE_INSENSITIVE);
    private Message title;
    private Message subTitle;
    private long fadeIn;
    private long stay;
    private long fadeOut;

    private TitleST(Message title, Message subTitle, long fadeIn, long stay, long fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public static TitleST of(Message title) {
        return of(title, null);
    }

    public static TitleST of(Message title, Message subtitle) {
        return of(title, subtitle, DEFAULT_FADE_IN, DEFAULT_STAY, DEFAULT_FADE_OUT);
    }

    public static TitleST of(Message title, Message subTitle, long fadeIn, long stay, long fadeOut) {
        return new TitleST(title, subTitle, fadeIn, stay, fadeOut);
    }

    public static TitleST valueOf(String str) {
        Matcher matcher = PATTERN.matcher(str);
        if (matcher.matches()) {
            String title = matcher.group("title");
            String subTitle = null;
            Matcher msgMatcher = MESSAGE_PATTERN.matcher(title);
            if (msgMatcher.matches()) {
                title = msgMatcher.group("title");
                if (msgMatcher.groupCount() > 1) {
                    subTitle = msgMatcher.group("subtitle");
                }
            }

            if (matcher.groupCount() > 1) {
                return TitleST.of(Messaging.get().asConfigMessage(title),
                        Messaging.get().asConfigMessage(subTitle),
                        (long) Double.parseDouble(matcher.group("fadein")),
                        (long) Double.parseDouble(matcher.group("stay")),
                        (long) Double.parseDouble(matcher.group("fadeout")));
            } else {
                return TitleST.of(Messaging.get().asConfigMessage(title), Messaging.get().asConfigMessage(subTitle));
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.title.getMessage() + (this.subTitle == null ? "" : "\\n" + this.subTitle.getMessage()) + "::" + this.fadeIn + "::" + this.stay + "::" + this.fadeOut;
    }

    public Message getTitle() {
        return title;
    }

    public void setTitle(Message title) {
        this.title = title;
    }

    public Message getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(Message subTitle) {
        this.subTitle = subTitle;
    }

    public long getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(long fadeIn) {
        this.fadeIn = fadeIn;
    }

    public long getStay() {
        return stay;
    }

    public void setStay(long stay) {
        this.stay = stay;
    }

    public long getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(long fadeOut) {
        this.fadeOut = fadeOut;
    }

    public void send(AbstractMessaging member) {
        if (this.subTitle == null) {
            member.sendTitle(this.title, Title.Times.times(Ticks.duration(this.fadeIn), Ticks.duration(this.stay), Ticks.duration(this.fadeOut)));
        } else {
            member.sendTitle(this.title, this.subTitle, Title.Times.times(Ticks.duration(this.fadeIn), Ticks.duration(this.stay), Ticks.duration(this.fadeOut)));
        }
    }

    @Override
    public TitleST clone() {
        return new TitleST(this.title, this.subTitle, this.fadeIn, this.stay, this.fadeOut);
    }

}
