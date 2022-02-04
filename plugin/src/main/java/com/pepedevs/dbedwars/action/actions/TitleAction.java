package com.pepedevs.dbedwars.action.actions;

import com.pepedevs.dbedwars.api.action.Action;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.messaging.AbstractMessaging;
import net.kyori.adventure.title.Title;

import java.time.Duration;

public class TitleAction implements Action<AbstractMessaging> {

    private final Message title;
    private final Message subtitle;
    private final Title.Times times;

    public TitleAction(Message title) {
        this.title = title;
        this.subtitle = Message.empty();
        this.times = Title.DEFAULT_TIMES;
    }

    public TitleAction(Message title, Message subtitle) {
        this.title = title;
        this.subtitle = subtitle;
        this.times = Title.DEFAULT_TIMES;
    }

    public TitleAction(Message title, Message subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.times = Title.Times.times(Duration.ofMillis(fadeIn * 50L), Duration.ofSeconds(stay * 50L), Duration.ofMillis(fadeOut * 50L));
    }

    @Override
    public void execute(AbstractMessaging abstractMessaging) {
        abstractMessaging.sendTitle(title, subtitle, times);
    }

}
