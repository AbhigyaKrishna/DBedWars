package org.zibble.dbedwars.game.arena;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.zibble.dbedwars.api.game.GameEvent;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionProvider;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableEvents;
import org.zibble.dbedwars.script.ScriptRegistryImpl;

public class GameEventImpl implements GameEvent {

    private final Key key;
    public Duration duration;
    private Message eventName;
    private final Multimap<Event, ActionProvider> actions = MultimapBuilder.enumKeys(Event.class).linkedListValues().build();

    public GameEventImpl(Key key, Message eventName, Duration duration) {
        this.key = key;
        this.eventName = eventName;
        this.duration = duration;
    }

    public static GameEventImpl fromConfig(String key, ConfigurableEvents.Event config) {
        GameEventImpl event = new GameEventImpl(Key.of(key), ConfigMessage.from(config.getName()), Duration.valueOf(config.getDuration()));
        for (String s : config.getOnStart()) {
            event.addAction(Event.ON_START, ActionProvider.fromString(ScriptRegistryImpl.GAME_EVENT, s));
        }
        for (String s : config.getPerTick()) {
            event.addAction(Event.PER_TICK, ActionProvider.fromString(ScriptRegistryImpl.GAME_EVENT, s));
        }
        for (String s : config.getOnEnd()) {
            event.addAction(Event.ON_END, ActionProvider.fromString(ScriptRegistryImpl.GAME_EVENT, s));
        }
        return event;
    }

    @Override
    public Message getEventName() {
        return eventName;
    }

    @Override
    public void setEventName(Message eventName) {
        this.eventName = eventName;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Multimap<Event, ActionProvider> getActions() {
        return actions;
    }

    @Override
    public void addAction(Event event, ActionProvider action) {
        actions.put(event, action);
    }

    public void perform(Event event, ScriptVariable<?>... variables) {
        for (ActionProvider provider : this.actions.get(event)) {
            provider.provide(variables);
        }
    }

    @Override
    public Key getKey() {
        return this.key;
    }

}
