package org.zibble.dbedwars.api.game;

import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.script.action.ActionProvider;
import org.zibble.dbedwars.api.util.key.Keyed;

public interface GameEvent extends Keyed {

    Message getEventName();

    void setEventName(Message eventName);

    Duration getDuration();

    void setDuration(Duration duration);

    void addAction(Event event, ActionProvider action);

    enum Event {
        ON_START,
        PER_TICK,
        ON_END,
        ;
    }

}
