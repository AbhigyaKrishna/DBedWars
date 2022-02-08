package com.pepedevs.dbedwars.api.util;

import com.pepedevs.dbedwars.api.messaging.message.AdventureMessage;
import com.pepedevs.dbedwars.api.messaging.message.Message;

/*TODO REWORK*/
public class TitleST {

    private static final String SEPARATOR = ";";

    private final Message title;
    private final Message subTitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    private TitleST(Message title, Message subTitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public static TitleST of(Message title, Message subTitle, int fadeIn, int stay, int fadeOut) {
        return new TitleST(title, subTitle, fadeIn, stay, fadeOut);
    }

    public static TitleST of(String title) {
        String[] split = title.split(SEPARATOR);
        return new TitleST(
                AdventureMessage.from(split[0]),
                split.length > 1 ? AdventureMessage.from(split[1]) : null,
                split.length > 2 ? Integer.parseInt(split[2]) : -1,
                split.length > 3 ? Integer.parseInt(split[3]) : -1,
                split.length > 4 ? Integer.parseInt(split[4]) : -1
        );
    }

    @Override
    public String toString() {
        return this.title.getMessage() +
                SEPARATOR +
                (this.subTitle == null ? "" : this.subTitle.getMessage()) +
                SEPARATOR + (this.fadeIn == -1 ? "" : this.fadeIn) +
                SEPARATOR + (this.stay == -1 ? "" : this.stay) +
                SEPARATOR + (this.fadeOut == -1 ? "" : this.fadeOut);
    }

    public Message getTitle() {
        return title;
    }

    public Message getSubTitle() {
        return subTitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }
}
