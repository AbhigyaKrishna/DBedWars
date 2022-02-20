package org.zibble.dbedwars.action.actions;

import net.kyori.adventure.title.Title;
import org.zibble.dbedwars.api.action.Action;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.messaging.AbstractMessaging;

import java.time.Duration;

public class TitleAction implements Action<AbstractMessaging> {

    private final AbstractMessaging abstractMessaging;
    private final Message title;
    private final Message subtitle;
    private final Title.Times times;

    public TitleAction(Message title, AbstractMessaging abstractMessaging) {
        this.abstractMessaging = abstractMessaging;
        this.title = title;
        this.subtitle = Message.empty();
        this.times = Title.DEFAULT_TIMES;
    }

    public TitleAction(Message title, Message subtitle, AbstractMessaging abstractMessaging) {
        this.abstractMessaging = abstractMessaging;
        this.title = title;
        this.subtitle = subtitle;
        this.times = Title.DEFAULT_TIMES;
    }

    public TitleAction(Message title, Message subtitle, int fadeIn, int stay, int fadeOut, AbstractMessaging abstractMessaging) {
        this.abstractMessaging = abstractMessaging;
        this.title = title;
        this.subtitle = subtitle;
        this.times = Title.Times.times(Duration.ofMillis(fadeIn * 50L), Duration.ofSeconds(stay * 50L), Duration.ofMillis(fadeOut * 50L));
    }

    @Override
    public void execute() {
        this.getHandle().sendTitle(title, subtitle, times);
    }

    @Override
    public AbstractMessaging getHandle() {
        return this.abstractMessaging;
    }

    public Message getTitle() {
        return title;
    }

    public Message getSubtitle() {
        return subtitle;
    }

    public Title.Times getTimes() {
        return times;
    }

}
