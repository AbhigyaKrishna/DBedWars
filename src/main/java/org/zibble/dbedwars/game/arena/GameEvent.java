package org.zibble.dbedwars.game.arena;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableEvents;
import org.zibble.dbedwars.script.action.ActionPreProcessor;
import org.zibble.dbedwars.utils.TimeUtil;

public class GameEvent {

    public Duration duration;
    private Message eventName;
    private Multimap<Event, String> actions = MultimapBuilder.enumKeys(Event.class).linkedListValues().build();

    public GameEvent(Message eventName, Duration duration) {
        this.eventName = eventName;
        this.duration = duration;
    }

    public static GameEvent fromConfig(ConfigurableEvents.Event config) {
        GameEvent event = new GameEvent(ConfigMessage.from(config.getName()), TimeUtil.parse(config.getDuration()));
        event.actions.putAll(Event.ON_START, config.getOnStart());
        event.actions.putAll(Event.PER_TICK, config.getPerTick());
        event.actions.putAll(Event.ON_END, config.getOnEnd());
        return event;
    }

    public Message getEventName() {
        return eventName;
    }

    public void setEventName(Message eventName) {
        this.eventName = eventName;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Multimap<Event, String> getActions() {
        return actions;
    }

    public void perform(Event event, ScriptVariable<?>... variables) {
        for (String s : this.actions.get(event)) {
            ActionPreProcessor.process(Key.of("game_event"), s, variables).execute();
        }
    }

    public enum Event {
        ON_START,
        PER_TICK,
        ON_END,
        ;
    }

}
